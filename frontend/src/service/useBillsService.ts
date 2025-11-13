import { useApiError } from "@/axios/useApiError";
import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import { AxiosError } from "axios";
import { useCallback } from "react";
import Bill, { CreateBill, UpdateBill } from "@data/Bills";

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

  const createBill = useCallback(
    async (bill: CreateBill): Promise<Bill | Message | null> =>
      safeRequest(() =>
        api.post<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const listBill = useCallback(
    async (eventUuid: string): Promise<Bill | null> =>
      api.get<Bill>(`/events/${eventUuid}/bills`).then((res) => res.data),
    [api],
  );

  const updateBill = useCallback(
    async (bill: UpdateBill): Promise<Bill | Message | null> =>
      safeRequest(() =>
        api.put<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const deleteBill = useCallback(
    async (billUuid: string): Promise<void | Message | null> =>
      safeRequest(() =>
        api.delete<void>(`/events/bills/${billUuid}`).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  return {
    createBill,
    listBill,
    updateBill,
    deleteBill,
  };
};

export default useUserService;
