import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import { useCallback } from "react";
import { PaginatedResponse } from "../data/PagesType";
import EventInvitation, {
  AcceptInvitation,
  UserInvitation,
} from "@data/EventInvitation";
import { useSafeRequest } from "./useHandleRequest";

const useEventInvitationService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const userEventInvitation = useCallback(
    async (
      invitation: UserInvitation,
    ): Promise<EventInvitation | Message | null> =>
      safeRequest(() =>
        api
          .post<EventInvitation>("/events/invitation", invitation)
          .then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const acceptedEventInvitation = useCallback(
    async (
      acceptInvitation: AcceptInvitation,
    ): Promise<EventInvitation | Message | null> =>
      safeRequest(() =>
        api
          .post<EventInvitation>(
            "/events/invitation/accepted",
            acceptInvitation,
          )
          .then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const listEventInvitations = useCallback(
    async (
      page?: number,
      size?: number,
      sort?: string,
    ): Promise<PaginatedResponse<EventInvitation> | null> =>
      api
        .get<
          PaginatedResponse<EventInvitation>
        >("/events/invitations", { params: { page, size, sort } })
        .then((res) => res.data),
    [api, safeRequest],
  );

  return {
    userEventInvitation,
    acceptedEventInvitation,
    listEventInvitations,
  };
};

export default useEventInvitationService;
