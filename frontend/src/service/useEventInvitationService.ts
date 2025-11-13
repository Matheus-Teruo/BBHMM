import { useApiError } from "@/axios/useApiError";
import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import { AxiosError } from "axios";
import { useCallback } from "react";
import { PaginatedResponse } from "./PagesType";
import EventInvitation, {
  AcceptInvitation,
  UserInvitation,
} from "@data/EventInvitation";

const useEventInvitationService = () => {
  const api = useAxios();
  const handleApiError = useApiError();

  const safeRequest = useCallback(
    async <T>(fn: () => Promise<T>): Promise<T | Message | null> => {
      try {
        return await fn();
      } catch (error) {
        handleApiError(error);
        if (error instanceof AxiosError) {
          return error.response!.data as Message;
        }
        return null;
      }
    },
    [handleApiError],
  );

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

  const listEvents = useCallback(
    async (
      page?: number,
      size?: number,
      sort?: string,
    ): Promise<PaginatedResponse<Event> | null> =>
      api
        .get<
          PaginatedResponse<Event>
        >("/events/invitations", { params: { page, size, sort } })
        .then((res) => res.data),
    [api, safeRequest],
  );

  return {
    userEventInvitation,
    acceptedEventInvitation,
    listEvents,
  };
};

export default useEventInvitationService;
