import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import Event, { CreateEvent, UpdateEvent } from "@data/Event";
import { useCallback } from "react";
import { PaginatedResponse } from "../data/PagesType";
import { UserResume } from "@data/User";
import { useSafeRequest } from "./useHandleRequest";

const useEventService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const createEvent = useCallback(
    async (event: CreateEvent): Promise<Event | Message | null> =>
      safeRequest(() =>
        api.post<Event>("/events", event).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getEvent = useCallback(
    async (eventUuid: string): Promise<Event | null> =>
      api.get<Event>(`/events/${eventUuid}`).then((res) => res.data),
    [api],
  );

  const getEvents = useCallback(
    async (
      page?: number,
      size?: number,
      sort?: string,
    ): Promise<PaginatedResponse<Event> | null> =>
      api
        .get<
          PaginatedResponse<Event>
        >("/events", { params: { page, size, sort } })
        .then((res) => res.data),
    [api, safeRequest],
  );

  const updateEvent = useCallback(
    async (event: UpdateEvent): Promise<Event | Message | null> =>
      safeRequest(() =>
        api.put<Event>("/events", event).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const listUserFromEvent = useCallback(
    async (eventUuid: string): Promise<UserResume[] | null> =>
      api
        .get<UserResume[]>(`/events/${eventUuid}/users`)
        .then((res) => res.data),
    [api],
  );

  return {
    createEvent,
    getEvent,
    getEvents,
    updateEvent,
    listUserFromEvent,
  };
};

export default useEventService;
