import { useApiError } from "@/axios/useApiError";
import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import Payment, { PayBill } from "@data/Payment";
import { AxiosError } from "axios";
import { useCallback } from "react";

const usePaymentService = () => {
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

  const getPayment = useCallback(
    async (eventUuid: string): Promise<Payment | null> =>
      api.get<Payment>(`/events/${eventUuid}/payment`).then((res) => res.data),
    [api],
  );

  const makePayment = useCallback(
    async (payBill: PayBill): Promise<void | Message | null> =>
      safeRequest(() =>
        api.post<void>("/events/payment", payBill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  return {
    getPayment,
    makePayment,
  };
};

export default usePaymentService;
