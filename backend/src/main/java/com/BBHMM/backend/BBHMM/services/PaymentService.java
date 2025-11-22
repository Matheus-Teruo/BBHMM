package com.BBHMM.backend.BBHMM.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Participants;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.UserBalance;
import com.BBHMM.backend.BBHMM.models.request.PayBillRequest;
import com.BBHMM.backend.BBHMM.models.response.PaymentResponse;
import com.BBHMM.backend.BBHMM.repositories.BillRepository;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillValidation billValidation;
    private final BillRepository billRepository;
    private final UserService userService;
    private final EventService eventService;

    public List<PaymentResponse> getPaymenList(UUID eventuuid, User userOwner) {
        billValidation.checkUserParticipationInEvent(userOwner, eventuuid);
        List<Bill> bills = billRepository.findBillsByEventUuidAndNotPaid(eventuuid);
        List<Participants> participants = billRepository.findUnpaidParticipants();

        Map<UUID, BigDecimal> paymentMap = new HashMap<>();
        for (Bill bill : bills) {
            addTotal(paymentMap, bill.getPayerUuid(), bill.getRemainingBalance());
        }

        Map<UUID, BigDecimal> participantsMap = new HashMap<>();
        for (Participants participant : participants) {
            addTotal(participantsMap, participant.getUserUuid(), participant.getRemainingBalance());
        }

        List<UserBalance> receivers = paymentMap.entrySet().stream()
            .map(e -> new UserBalance(e.getKey(), e.getValue()))
            .sorted(Comparator.comparing(UserBalance::getBalance).reversed())
            .toList();

        List<UserBalance> payers = participantsMap.entrySet().stream()
            .map(e -> new UserBalance(e.getKey(), e.getValue()))
            .sorted(Comparator.comparing(UserBalance::getBalance).reversed())
            .toList();

        List<PaymentResponse> payments = new ArrayList<>();

        int i = 0, j = 0;
        while (i < payers.size() && j < receivers.size()) {
            UserBalance payer = payers.get(i);
            UserBalance receiver = receivers.get(j);

            if (payer.getBalance().compareTo(BigDecimal.ZERO) == 0) { i++; continue; }
            if (receiver.getBalance().compareTo(BigDecimal.ZERO) == 0) { j++; continue; }

            BigDecimal amount = payer.getBalance().min(receiver.getBalance());

            payer.setBalance(payer.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().subtract(amount));

            payments.add(new PaymentResponse(payer.getUserUuid(), amount, receiver.getUserUuid()));

            if (payer.getBalance().compareTo(BigDecimal.ZERO) == 0) i++;
            if (receiver.getBalance().compareTo(BigDecimal.ZERO) == 0) j++;
        }

        return payments.stream()
            .filter(payment -> payment.userToPayUuid().equals(userOwner.getUuid()))
            .toList();
    }

    @Transactional
    public void payOffDebit(PayBillRequest request, User userOwner) {
        var user = userService.safeTakeUserByUuid(userOwner.getUuid());
        var event = eventService.safeTakeEventByUuid(request.eventuUuid());
        billValidation.checkUserParticipationInEvent(user, event.getUuid());

        Bill bill = new Bill(request, event, user);
        Participants participants = new Participants(request, user, bill);
        bill.addParticipant(participants);
        billRepository.save(bill);
    }

    private void addTotal(Map<UUID, BigDecimal> mapa, UUID userUuid, BigDecimal value) {
        mapa.merge(userUuid, value, BigDecimal::add);
    }
}
