import { useApiError } from "@/axios/useApiError";
import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import { CreateEvent, UpdateEvent } from "@data/Event";
import { AxiosError } from "axios";
import { useCallback } from "react";
import { PaginatedResponse } from "./PagesType";

const useUserService = () => {
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
    async (eventUuid: string): Promise<Event[] | null> =>
      api.get<Event[]>(`/events/${eventUuid}/users`).then((res) => res.data),
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

export default useUserService;
