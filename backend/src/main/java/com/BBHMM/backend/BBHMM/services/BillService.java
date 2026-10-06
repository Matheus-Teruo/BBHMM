package com.BBHMM.backend.BBHMM.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Event;
import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;
import com.BBHMM.backend.BBHMM.repositories.BillRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository repository;
    private final BillValidation validation;
    private final EventService eventService;
    private final EventUserService eventUserService;
    private final UserService userService;

    @Transactional
    public Bill createBill(CreateBillRequest request) {
        User userSecurity = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userService.safeTakeUserByUuid(request.payerUuid());
        var event = eventService.safeTakeEventByUuid(request.eventUuid());
        validation.checkUserParticipationInEvent(userSecurity, event.getUuid());
        validation.checkUserParticipationInEvent(user, event.getUuid());
        validation.checkEventFinished(event);

        var bill = new Bill(request, event, user);
        updateBillPaidValue(bill, event, userSecurity);
        repository.save(bill);

        return bill;
    }

    public Bill getBill(UUID uuid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var bill = repository.findByUuid(uuid)
            .orElseThrow(EntityNotFoundException::new);
        validation.checkUserParticipationInEvent(user, bill.getEventUuid());
        return bill;
    }

    public Bill safeTakeBillByUuid(UUID uuid) {
    return repository.findByUuidWithParticipants(uuid)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Conta não encontrado",
            "conta inexistente",
            "ID",
            uuid.toString())
        );
    }

    public List<Bill> listBillsByEvent(UUID eventUuid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user, eventUuid);
        return repository.findBillsByEventUuid(eventUuid);
    }

    public List<Participants> listParticipantsByEvent(UUID eventUuid) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user, eventUuid);
        return repository.findParticipantsByEventUuid(eventUuid);
    }

    @Transactional
    public Bill updateBill(UpdateBillRequest request) {
        var bill = safeTakeBillByUuid(request.uuid());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user, bill.getEventUuid());
        validation.checkEventFinished(bill.getEvent());

        bill.update(request);
        if (request.listPartUuids() != null) {
            validation.checkUsersParticipationInEvent(bill.getEventUuid(), request.listPartUuids());
            updateParticipants(bill, request.listPartUuids(), request.value(), user);
        }

        return bill;
    }

    @Transactional
    public void deleteBill(UUID billUuid) {
        var bill = safeTakeBillByUuid(billUuid);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user, bill.getEventUuid());
        validation.checkEventFinished(bill.getEvent());

        List<Participants> currentParticipants = bill.getParticipants();

        reversePaidValue(currentParticipants, bill, user);
        reverseBillPaidValue(bill, bill.getEvent(), user);
        repository.delete(bill);
    }

    public void updateParticipants(Bill bill, List<UUID> listPartUuids, BigDecimal value, User userOwner) {
        List<Participants> currentParticipants = bill.getParticipants();

        reversePaidValue(currentParticipants, bill, userOwner);

        Set<UUID> newUuids = new HashSet<>(listPartUuids);
        Set<UUID> currentUuids = currentParticipants.stream()
                .map(p -> p.getUserUuid())
                .collect(Collectors.toSet());

        List<Participants> toRemove = currentParticipants.stream()
                .filter(p -> !newUuids.contains(p.getUserUuid()))
                .toList();

        currentParticipants.removeAll(toRemove);

        List<Participants> toAdd = newUuids.stream()
                .filter(uuid -> !currentUuids.contains(uuid))
                .map(uuid -> {
                    User user = userService.safeTakeUserByUuid(uuid);
                    Participants p = new Participants(user, bill);
                    return p;
                })
                .toList();

        currentParticipants.addAll(toAdd);

        updatePaidValue(currentParticipants, bill, value, userOwner);

        bill.setParticipants(currentParticipants);
    }

    public void updateParticipants(Bill bill, User participant, boolean add, BigDecimal value, User userOwner) {

        List<Participants> currentParticipants = bill.getParticipants();

        reversePaidValue(currentParticipants, bill, userOwner);

        if (add) {
            boolean alreadyExists = currentParticipants.stream()
                    .anyMatch(p -> p.getUserUuid().equals(participant.getUuid()));

            if (!alreadyExists) {
                Participants newParticipant = new Participants(participant, bill);
                currentParticipants.add(newParticipant);
            }

        } else {
            currentParticipants.removeIf(p -> p.getUserUuid().equals(participant.getUuid()));
        }

        if (currentParticipants.isEmpty()) {
            bill.setParticipants(currentParticipants);
            return;
        }

        updatePaidValue(currentParticipants, bill, value, userOwner);

        bill.setParticipants(currentParticipants);
    }

    private void updatePaidValue(List<Participants> currentParticipants, Bill bill, BigDecimal value, User userOwner) {
        int size = currentParticipants.size();
        List<EventUser> eventUsers = eventUserService.listEventUsers(bill.getEventUuid(), userOwner);
        EventUser eventUserOwner = eventUserService.findEventUser(userOwner, bill.getEventUuid());
        BigDecimal currentValue = value;
        for (Participants participation : currentParticipants) {
            EventUser eventUser = eventUsers.stream()
                .filter(eu -> eu.getUserUuid().equals(
                    participation.getUserUuid()
                ))
                .findFirst()
                .orElse(null);
            BigDecimal share = currentValue.divide(
                BigDecimal.valueOf(size--),
                2,
                RoundingMode.DOWN
            );
            currentValue = currentValue.subtract(share);
            participation.setValue(share);
            BigDecimal userDebit = share;
            List<Bill> userBills = repository.findBillsByPayerUuidAndNotPaid(participation.getUserUuid(), bill.getEventUuid());
            for (Bill userBill : userBills) {
                BigDecimal debtBill = userBill.getRemainingBalance();
                if (userDebit.compareTo(debtBill) < 0) {
                    participation.completeParticipation();
                    userBill.addToDebit(userDebit);
                    eventUser.addDebit(currentValue);
                    eventUserOwner.addCredit(currentValue);
                    userDebit = BigDecimal.ZERO;
                } else if (userDebit.compareTo(debtBill) >= 0) {
                    userBill.completeBill();
                    eventUser.addDebit(currentValue);
                    eventUserOwner.addCredit(currentValue);
                    if (userDebit.compareTo(debtBill) == 0) {
                        participation.completeParticipation();
                    } else {
                        participation.addPaidValue(debtBill);
                    }
                    userDebit = userDebit.subtract(debtBill);
                }
                if (userDebit.compareTo(BigDecimal.ZERO) == 0) {
                    participation.setPaid(true);
                    break;
                }
            }
        }
    }

    public void reversePaidValue(List<Participants> removedParticipants, Bill bill, User user) {
        List<EventUser> eventUsers = eventUserService.listEventUsers(bill.getEventUuid(), user);
        EventUser eventUserOwner = eventUserService.findEventUser(user, bill.getEventUuid());
        for (Participants participation : removedParticipants) {
            EventUser eventUser = eventUsers.stream()
                .filter(eu -> eu.getUserUuid().equals(
                    participation.getUserUuid()
                ))
                .findFirst()
                .orElse(null);
            BigDecimal undoPaidValue = participation.getPaidValue();
            List<Bill> userBills = repository.findBillsByPayerUuidAndPaid(participation.getUserUuid(), bill.getEventUuid());
            for (Bill userBill : userBills) {
                BigDecimal undoDebtBill = userBill.getDebitAmount();
                if (undoPaidValue.compareTo(undoDebtBill) < 0) {
                    participation.undoParticipation();
                    userBill.subtractToDebit(undoPaidValue);
                    eventUser.subtractDebit(undoPaidValue);
                    eventUserOwner.subtractCredit(undoPaidValue);
                    undoPaidValue = BigDecimal.ZERO;
                } else if (undoPaidValue.compareTo(undoDebtBill) >= 0) {
                    userBill.undoCompleteBill();
                    eventUser.subtractDebit(undoPaidValue);
                    eventUserOwner.subtractCredit(undoPaidValue);
                    if (undoPaidValue.compareTo(undoDebtBill) == 0) {
                        participation.undoParticipation();
                    } else {
                        participation.subtractPaidValue(undoDebtBill);
                    }
                    undoPaidValue = undoPaidValue.subtract(undoDebtBill);
                }
                if (undoPaidValue.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
        }
    }

    private void updateBillPaidValue(Bill bill, Event event, User user) {
        List<Participants> userParticipants = repository.findUnpaidParticipantsByUser(bill.getPayerUuid(), event.getUuid());
        List<EventUser> eventUsers = eventUserService.listEventUsers(event.getUuid(), user);
        EventUser eventUserOwner = eventUserService.findEventUser(user, event.getUuid());
        BigDecimal userDebit = bill.getValue();
        for (Participants userParticipation : userParticipants) {
            EventUser eventUser = eventUsers.stream()
                .filter(eu -> eu.getUserUuid().equals(
                    userParticipation.getUserUuid()
                ))
                .findFirst()
                .orElse(null);
            BigDecimal debtParticipation = userParticipation.getRemainingBalance();
            if (userDebit.compareTo(debtParticipation) < 0) {
                bill.completeBill();
                userParticipation.addPaidValue(userDebit);
                eventUser.addDebit(userDebit);
                eventUserOwner.addCredit(userDebit);
                userDebit = BigDecimal.ZERO;
            } else if (userDebit.compareTo(debtParticipation) >= 0) {
                userParticipation.completeParticipation();
                eventUser.addDebit(userDebit);
                eventUserOwner.addCredit(userDebit);
                if (userDebit.compareTo(debtParticipation) == 0) {
                    bill.completeBill();
                } else {
                    bill.addToDebit(debtParticipation);
                }
                userDebit = userDebit.subtract(debtParticipation);
            }
            if (userDebit.compareTo(BigDecimal.ZERO) == 0) {
                bill.setPaid(true);
                break;
            }
        }
    }

    private void reverseBillPaidValue(Bill bill, Event event, User user) {
        List<Participants> userParticipants = repository.findPaidParticipantsByUser(bill.getPayerUuid(), bill.getEventUuid());
        List<EventUser> eventUsers = eventUserService.listEventUsers(event.getUuid(), user);
        BigDecimal undoDebitAmount = bill.getDebitAmount();
        EventUser eventUserOwner = eventUserService.findEventUser(user, event.getUuid());
        for (Participants userParticipation : userParticipants) {
            EventUser eventUser = eventUsers.stream()
                .filter(eu -> eu.getUserUuid().equals(
                    userParticipation.getUserUuid()
                ))
                .findFirst()
                .orElse(null);
            BigDecimal undoDebtBill = userParticipation.getPaidValue();
            if (undoDebitAmount.compareTo(undoDebtBill) < 0) {
                bill.undoCompleteBill();
                userParticipation.subtractPaidValue(undoDebitAmount);
                eventUser.subtractDebit(undoDebitAmount);
                eventUserOwner.subtractCredit(undoDebitAmount);
                undoDebitAmount = BigDecimal.ZERO;
            } else if (undoDebitAmount.compareTo(undoDebtBill) >= 0) {
                userParticipation.undoParticipation();
                eventUser.subtractDebit(undoDebitAmount);
                eventUserOwner.subtractCredit(undoDebitAmount);
                if (undoDebitAmount.compareTo(undoDebtBill) == 0) {
                    bill.undoCompleteBill();
                } else {
                    bill.subtractToDebit(undoDebtBill);
                }
                undoDebitAmount = undoDebitAmount.subtract(undoDebtBill);
            }
            if (undoDebitAmount.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }
    }
}
