import useAxios from "@/axios/useAxios";
import Payment, { DebitTotal, PayBill, PaymentDetails } from "@data/Payment";
import { useCallback } from "react";
import { useSafeRequest } from "../axios/useHandleRequest";
import ApiError from "@data/Error";

const usePaymentService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const getDebitTotal = useCallback(
    async (eventUuid: string): Promise<DebitTotal> =>
      api.get<DebitTotal>(`/events/${eventUuid}/total`).then((res) => res.data),
    [api],
  );

  const getPayment = useCallback(
    async (eventUuid: string): Promise<Payment[]> =>
      api
        .get<Payment[]>(`/events/${eventUuid}/payment`)
        .then((res) => res.data),
    [api],
  );

  const getReceiving = useCallback(
    async (eventUuid: string): Promise<Payment[]> =>
      api
        .get<Payment[]>(`/events/${eventUuid}/receiving`)
        .then((res) => res.data),
    [api],
  );

  const makePayment = useCallback(
    async (payBill: PayBill): Promise<void | ApiError> =>
      safeRequest(() =>
        api.post<void>("/events/payment", payBill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const listPayroll = useCallback(
    async (eventUuid: string): Promise<PaymentDetails[]> =>
      api
        .get<PaymentDetails[]>(`/events/${eventUuid}/payment/list`)
        .then((res) => res.data),
    [api],
  );

  return {
    getDebitTotal,
    getPayment,
    getReceiving,
    makePayment,
    listPayroll,
  };
};

export default usePaymentService;
