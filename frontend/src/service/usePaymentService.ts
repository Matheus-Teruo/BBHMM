import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import Payment, { DebitTotal, PayBill } from "@data/Payment";
import { useCallback } from "react";
import { useSafeRequest } from "./useHandleRequest";

const usePaymentService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const getDebitTotal = useCallback(
    async (eventUuid: string): Promise<DebitTotal | null> =>
      api.get<DebitTotal>(`/events/${eventUuid}/total`).then((res) => res.data),
    [api],
  );

  const getPayment = useCallback(
    async (eventUuid: string): Promise<Payment[] | null> =>
      api
        .get<Payment[]>(`/events/${eventUuid}/payment`)
        .then((res) => res.data),
    [api],
  );

  const getReceiving = useCallback(
    async (eventUuid: string): Promise<Payment[] | null> =>
      api
        .get<Payment[]>(`/events/${eventUuid}/receiving`)
        .then((res) => res.data),
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
    getDebitTotal,
    getPayment,
    getReceiving,
    makePayment,
  };
};

export default usePaymentService;
