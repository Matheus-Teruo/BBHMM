package com.BBHMM.backend.BBHMM.services;

import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.infra.exceptions.InvalidDatabaseQueryException;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.request.BillCreateRequest;
import com.BBHMM.backend.BBHMM.models.request.BillUpdateRequest;
import com.BBHMM.backend.BBHMM.repositories.BillRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository repository;

    private final EventService eventService;

    private final UserService userService;

    @Transactional
    public Bill createBill(BillCreateRequest request) {
        var user = userService.safeTakeUserByUuid(request.payerUuid());
        var event = eventService.safeTakeEventByUuid(request.eventUuid());
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
    public Bill updateBill(BillUpdateRequest request) {
        var bill = safeTakeBillByUuid(request.uuid());

        

        return bill;
    }

    public void deleteBill() {

    }
}
