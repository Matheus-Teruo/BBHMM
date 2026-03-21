import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import Event, { CreateEvent, UpdateEvent, UpdateEventUser } from "@data/Event";
import { useCallback } from "react";
import { PaginatedResponse } from "../data/PagesType";
import { EventUser } from "@data/User";
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
      eventName?: string,
      eventDate?: string,
      finished?: boolean,
      page?: number,
      size?: number,
      sort?: string,
    ): Promise<PaginatedResponse<Event> | null> =>
      api
        .get<PaginatedResponse<Event>>("/events", {
          params: {
            eventName: eventName != "" ? eventName : undefined,
            eventDate: eventDate != "" ? eventDate : undefined,
            finished: finished ? undefined : finished,
            page,
            size,
            sort,
          },
        })
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

  const updateEventUser = useCallback(
    async (eventUuid: string, eventUser: UpdateEventUser): Promise<EventUser[] | Message | null> =>
      safeRequest(() =>
        api.put<EventUser[]>(`/events/${eventUuid}/user`, eventUser).then((res) => res.data),
      ),
    [api],
  );

  const finishEvent = useCallback(
    async (eventUuid: string): Promise<void | Message | null> =>
      safeRequest(() =>
        api.delete<void>(`/events/${eventUuid}`).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const listUserFromEvent = useCallback(
    async (eventUuid: string): Promise<EventUser[] | null> =>
      api.get<EventUser[]>(`/events/${eventUuid}/users`).then((res) => res.data),
    [api],
  );

  return {
    createEvent,
    getEvent,
    getEvents,
    updateEvent,
    updateEventUser,
    finishEvent,
    listUserFromEvent,
  };
};

export default useEventService;
