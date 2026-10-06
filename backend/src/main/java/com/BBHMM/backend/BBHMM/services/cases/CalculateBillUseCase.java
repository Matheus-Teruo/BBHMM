package com.BBHMM.backend.BBHMM.services.cases;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.models.EventUser;
import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.UpdateBillParticipantsRequest;
import com.BBHMM.backend.BBHMM.services.BillService;
import com.BBHMM.backend.BBHMM.services.UserService;
import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

@Service
@RequiredArgsConstructor
public class CalculateBillUseCase {

    private final BillService billService;
    private final BillValidation billValidation;
    private final UserService userService;
    
    @Transactional
    public void updateBillParticipants(UpdateBillParticipantsRequest request) {
        var bill = billService.safeTakeBillByUuid(request.uuid());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User partUser = userService.safeTakeUserByUuid(request.partUuid());
        billValidation.checkUserParticipationInEvent(user, bill.getEventUuid());
        billValidation.checkUserParticipationInEvent(partUser, bill.getEventUuid());
        billValidation.checkEventFinished(bill.getEvent());

        boolean plus = true;
        switch (request.type()) {
            case "add":
                plus = true;
                break;
            case "remove":
                plus = false;
                break;
            case "reset":
                billService.reversePaidValue(bill.getParticipants(), bill, user);
                bill.getParticipants().removeAll(bill.getParticipants());
                return;
            case "all":
                billService.reversePaidValue(bill.getParticipants(), bill, user);
                bill.getParticipants().removeAll(bill.getParticipants());
                billService.updateParticipants(bill, bill.getEvent().getEventUsers().stream().map(EventUser::getUserUuid).toList(), bill.getValue(), user);
                return;
            default:
                return;
        }

        billService.updateParticipants(bill, partUser, plus, bill.getValue(), user);
    }
}
