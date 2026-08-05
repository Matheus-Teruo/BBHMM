package com.BBHMM.backend.BBHMM.services.cases;

import org.springframework.stereotype.Service;

import com.BBHMM.backend.BBHMM.models.User;
import com.BBHMM.backend.BBHMM.models.request.ResetGuestTokenRequest;
import com.BBHMM.backend.BBHMM.models.response.NewGuestResponse;
import com.BBHMM.backend.BBHMM.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnCaseGuest {

    private final UserService userService;
    
    public NewGuestResponse caseUpdateGuestToken(ResetGuestTokenRequest request, User hostUser) {
        String password = userService.generatePassword(8);
        User user = userService.getGuest(request.guestUuid(), hostUser, request.eventUuid(), password);

        return new NewGuestResponse(user, password);
    }
}
