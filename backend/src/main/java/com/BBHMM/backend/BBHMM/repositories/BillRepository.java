package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.BBHMM.backend.BBHMM.models.Bill;
import com.BBHMM.backend.BBHMM.models.Participants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID>{    
    
    @Query("SELECT b FROM Bill b WHERE b.uuid = :uuid")
    Optional<Bill> findByUuid(UUID uuid);

    @Query("""
            SELECT DISTINCT b
            FROM Bill b
            LEFT JOIN FETCH b.participants p
            WHERE b.uuid = :uuid
        """)
    Optional<Bill> findByUuidWithParticipants(UUID uuid);

    @Query("SELECT b FROM Bill b WHERE b.eventUuid = :eventUuid AND b.type = com.BBHMM.backend.BBHMM.models.BillType.BILL")
    List<Bill> findBillsByEventUuid(UUID eventUuid);

    @Query("SELECT b FROM Bill b WHERE b.eventUuid = :eventUuid AND b.paid = false")
    List<Bill> findBillsByEventUuidAndNotPaid(UUID eventUuid);

    @Query("SELECT b FROM Bill b WHERE b.payerUuid = :payerUuid AND b.eventUuid = :eventUuid AND b.paid = false")
    List<Bill> findBillsByPayerUuidAndNotPaid(UUID payerUuid, UUID eventUuid);

    @Query("SELECT b FROM Bill b WHERE b.payerUuid = :payerUuid AND b.eventUuid = :eventUuid AND b.debitAmount > 0")
    List<Bill> findBillsByPayerUuidAndPaid(UUID payerUuid, UUID eventUuid);

    @Query("""
    SELECT DISTINCT b
    FROM Bill b
    LEFT JOIN b.participants p
    WHERE (b.payerUuid = :userUuid OR p.userUuid = :userUuid)
        AND b.eventUuid = :eventUuid
        AND b.type = com.BBHMM.backend.BBHMM.models.BillType.PAYMENT
    """)
    List<Bill> findPaymentByUserAndEventUuid(UUID userUuid, UUID eventUuid);

    @Query("SELECT p FROM Participants p WHERE p.paid = false AND p.userUuid = :partUuid AND p.bill.eventUuid = :eventUuid")
    List<Participants> findUnpaidParticipantsByUser(UUID partUuid, UUID eventUuid);

    @Query("SELECT p FROM Participants p WHERE p.userUuid = :partUuid AND p.bill.eventUuid = :eventUuid AND p.paidValue > 0")
    List<Participants> findPaidParticipantsByUser(UUID partUuid, UUID eventUuid);

    @Query("SELECT p FROM Participants p WHERE p.bill.eventUuid = :eventUuid AND p.bill.type = com.BBHMM.backend.BBHMM.models.BillType.BILL")
    List<Participants> findParticipantsByEventUuid(UUID eventUuid);

    @Query("SELECT p FROM Participants p WHERE p.paid = false AND p.bill.eventUuid = :eventUuid")
    List<Participants> findUnpaidParticipants(UUID eventUuid);
}
