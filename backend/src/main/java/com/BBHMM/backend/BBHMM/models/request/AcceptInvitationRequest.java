package com.BBHMM.backend.BBHMM.models.request;

import java.util.UUID;

public record AcceptInvitationRequest(
    UUID uuid,
    boolean accept
) {
}
