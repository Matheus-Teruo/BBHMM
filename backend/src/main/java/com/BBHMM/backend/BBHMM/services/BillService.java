package com.BBHMM.backend.BBHMM.services;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
import com.BBHMM.backend.BBHMM.models.request.DeleteBillRequest;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillRequest;
import com.BBHMM.backend.BBHMM.repositories.BillRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
    private final UserService userService;

    @Transactional
    public Bill createBill(CreateBillRequest request) {
        var user = userService.safeTakeUserByUuid(request.payerUuid());
        var event = eventService.safeTakeEventByUuid(request.eventUuid());
        validation.checkUserParticipationInEvent(user.getUuid(), event.getUuid());

        var bill = new Bill(request, event, user);
        repository.save(bill);

        return bill;
    }

    public Bill safeTakeBillByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
        .orElseThrow(() -> new InvalidDatabaseQueryException(
            "Conta não encontrado",
            "ID",
            uuid.toString())
        );
    }

    public List<Bill> listBillsByEvent(UUID eventUuid) {
        return repository.findBillsByEventUuid(eventUuid);
    }

    @Transactional
    public Bill updateBill(UpdateBillRequest request) {
        var bill = safeTakeBillByUuid(request.uuid());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user.getUuid(), bill.getEvent().getUuid());
        validation.checkUsersParticipationInEvent(bill.getEvent().getUuid(), request.listPartUuids());

        bill.update(request);
        updateParticipants(bill, request, bill.getPayer().getUuid());

        return bill;
    }

    @Transactional
    public void deleteBill(DeleteBillRequest request) {
        var bill = safeTakeBillByUuid(request.uuid());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validation.checkUserParticipationInEvent(user.getUuid(), bill.getEvent().getUuid());

        List<Participants> currentParticipants = bill.getParticipants();

        reversePaidValue(currentParticipants, bill, bill.getPayer().getUuid());

        repository.delete(bill);
    }

    @Transactional
    private void updateParticipants(Bill bill, UpdateBillRequest request, UUID ownerUuid) {
        List<Participants> currentParticipants = bill.getParticipants();

        reversePaidValue(currentParticipants, bill, ownerUuid);

        Set<UUID> newUuids = new HashSet<>(request.listPartUuids());
        Set<UUID> currentUuids = currentParticipants.stream()
                .map(p -> p.getUser().getUuid())
                .collect(Collectors.toSet());

        List<Participants> toRemove = currentParticipants.stream()
                .filter(p -> !newUuids.contains(p.getUser().getUuid()))
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

        BigDecimal share = request.value().divide(BigDecimal.valueOf(currentParticipants.size()), RoundingMode.HALF_UP);

        updatePaidValue(currentParticipants, bill, share, ownerUuid);

        bill.setParticipants(currentParticipants);
    }

    @Transactional
    private void updatePaidValue(List<Participants> currentParticipants, Bill bill, BigDecimal share, UUID ownerUuid) {
        for (Participants participation : currentParticipants) {
            participation.setValue(share);
            BigDecimal userDebit = share;
            List<Bill> userBills = repository.findBillsByPayerUuidAndNotPaid(ownerUuid, bill.getEvent().getUuid());
            for (Bill userBill : userBills) {
                BigDecimal debtBill = userBill.getValue().subtract(userBill.getDebitAmount());
                if (userDebit.compareTo(debtBill) < 0) {
                    participation.completeParticipation();
                    userBill.addToDebit(userDebit);
                    userDebit = BigDecimal.ZERO;
                } else if (userDebit.compareTo(debtBill) >= 0) {
                    userBill.completeBill();
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

    @Transactional
    private void reversePaidValue(List<Participants> removedParticipants, Bill bill, UUID ownerUuid) {
        for (Participants participation : removedParticipants) {
            BigDecimal undoPaidValue = participation.getPaidValue();
            List<Bill> userBills = repository.findBillsByPayerUuidAndPaid(ownerUuid, bill.getEvent().getUuid());
            for (Bill userBill : userBills) {
                BigDecimal undoDebtBill = userBill.getDebitAmount();
                if (undoPaidValue.compareTo(undoDebtBill) < 0) {
                    participation.undoParticipation();
                    userBill.subtractToDebit(undoPaidValue);
                    undoPaidValue = BigDecimal.ZERO;
                } else if (undoPaidValue.compareTo(undoDebtBill) >= 0) {
                    userBill.undoCompleteBill();
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
}
