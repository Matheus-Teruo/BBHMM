import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import { AxiosResponse } from "axios";
import { useCallback } from "react";
import Bill, {
  BillResume,
  CreateBill,
  UpdateBill,
  UpdateBillParticipants,
} from "@data/Bills";
import { useSafeRequest } from "./useHandleRequest";

const useBillsService = () => {
  const api = useAxios();
  const { safeRequest, safeRequestWithoutMessage } = useSafeRequest();

  const createBill = useCallback(
    async (bill: CreateBill): Promise<Bill | Message | null> =>
      safeRequest(() =>
        api.post<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getBill = useCallback(
    async (billUuid: string): Promise<Bill | null> =>
      api.get<Bill>(`/events/bills/${billUuid}`).then((res) => res.data),
    [api],
  );

  const listBills = useCallback(
    async (eventUuid: string): Promise<BillResume[] | null> =>
      api
        .get<BillResume[]>(`/events/${eventUuid}/bills`)
        .then((res) => res.data),
    [api],
  );

  const updateBill = useCallback(
    async (bill: UpdateBill): Promise<Bill | Message | null> =>
      safeRequest(() =>
        api.put<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const updateBillParticipant = useCallback(
    async (bill: UpdateBillParticipants): Promise<AxiosResponse<void> | null> =>
      safeRequestWithoutMessage(() =>
        api.put<void>("/events/bills/participants", bill).then((res) => res),
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
    getBill,
    listBills,
    updateBill,
    updateBillParticipant,
    deleteBill,
  };
};

export default useBillsService;
