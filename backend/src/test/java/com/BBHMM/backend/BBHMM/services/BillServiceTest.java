package com.BBHMM.backend.BBHMM.services;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.BBHMM.backend.BBHMM.BbhmmApplicationTests;
// import com.BBHMM.backend.BBHMM.factory.BillFactory;
// import com.BBHMM.backend.BBHMM.models.Bill;
// import com.BBHMM.backend.BBHMM.models.request.CreateBillRequest;
// import com.BBHMM.backend.BBHMM.repositories.BillRepository;
// import com.BBHMM.backend.BBHMM.services.validation.BillValidation;

class BillServiceTest extends BbhmmApplicationTests {
    
    // @MockitoBean
    // private BillRepository repository;
    // @MockitoBean
    // private BillValidation validation;
    // @MockitoBean
    // private EventService eventService;
    // @MockitoBean
    // private UserService userService;

    // @Autowired
    // private BillService billService;

    // @Test
    // void createBill_sucess() {
    //     // given
    //     CreateBillRequest request = BillFactory.createRequest();

    //     User userSecurity = UserFactory.createUser();
    //     User payer = UserFactory.createUser();
    //     Event event = EventFactory.createEvent();

    //     Authentication authentication = mock(Authentication.class);
    //     SecurityContext securityContext = mock(SecurityContext.class);

    //     when(securityContext.getAuthentication()).thenReturn(authentication);
    //     when(authentication.getPrincipal()).thenReturn(userSecurity);
    //     SecurityContextHolder.setContext(securityContext);

    //     when(userService.safeTakeUserByUuid(request.payerUuid()))
    //             .thenReturn(payer);

    //     when(eventService.safeTakeEventByUuid(request.eventUuid()))
    //             .thenReturn(event);

    //     doNothing().when(validation)
    //             .checkUserParticipationInEvent(any(User.class), any(UUID.class));

    //     doNothing().when(validation)
    //             .checkEventFinished(any(Event.class));

    //     when(repository.save(any(Bill.class)))
    //             .thenAnswer(invocation -> invocation.getArgument(0));

    //     // when
    //     Bill result = billService.createBill(request);

    //     // then
    //     assertThat(result).isNotNull();
    //     verify(repository, times(1)).save(any(Bill.class));
    // }
}
