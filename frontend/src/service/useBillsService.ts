import useAxios from "@/axios/useAxios";
import { useCallback } from "react";
import Bill, {
  BillResume,
  CreateBill,
  UpdateBill,
  UpdateBillParticipants,
} from "@data/Bills";
import { useSafeRequest } from "../axios/useHandleRequest";

const useBillsService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const createBill = useCallback(
    async (bill: CreateBill): Promise<Bill> =>
      safeRequest(() =>
        api.post<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getBill = useCallback(
    async (billUuid: string): Promise<Bill> =>
      api.get<Bill>(`/events/bills/${billUuid}`).then((res) => res.data),
    [api],
  );

  const listBills = useCallback(
    async (eventUuid: string): Promise<BillResume[]> =>
      api
        .get<BillResume[]>(`/events/${eventUuid}/bills`)
        .then((res) => res.data),
    [api],
  );

  const updateBill = useCallback(
    async (bill: UpdateBill): Promise<Bill> =>
      safeRequest(() =>
        api.put<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const updateBillParticipant = useCallback(
    async (bill: UpdateBillParticipants): Promise<void> =>
      safeRequest(() =>
        api.put<void>("/events/bills/participants", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const deleteBill = useCallback(
    async (billUuid: string): Promise<void> =>
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
