package com.BBHMM.backend.BBHMM.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.BBHMM.backend.BBHMM.models.Bill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID>{    
    
    @Query("SELECT b FROM Bills b WHERE b.uuid = :uuid")
    Optional<Bill> findByUuid(UUID uuid);

    @Query("SELECT b FROM Bills b WHERE b.event.uuid = :eventUuid")
    List<Bill> findBillsByEventUuid(UUID eventUuid);

    @Query("SELECT b FROM Bill b WHERE b.payer.uuid = :payerUuid AND b.event.uuid = :eventUuid AND b.paid = false")
    List<Bill> findBillsByPayerUuidAndNotPaid(UUID payerUuid, UUID eventUuid);

    @Query("SELECT b FROM Bill b WHERE b.payer.uuid = :payerUuid AND b.event.uuid = :eventUuid AND b.debitAmount > 0")
    List<Bill> findBillsByPayerUuidAndPaid(UUID payerUuid, UUID eventUuid);
}
