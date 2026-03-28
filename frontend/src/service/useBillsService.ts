import useAxios from "@/axios/useAxios";
import { AxiosResponse } from "axios";
import { useCallback } from "react";
import Bill, {
  BillResume,
  CreateBill,
  UpdateBill,
  UpdateBillParticipants,
} from "@data/Bills";
import { useSafeRequest } from "../axios/useHandleRequest";
import ApiError from "@data/Error";

const useBillsService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const createBill = useCallback(
    async (bill: CreateBill): Promise<Bill | ApiError> =>
      safeRequest(() =>
        api.post<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getBill = useCallback(
    async (billUuid: string): Promise<Bill | ApiError> =>
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
    async (bill: UpdateBill): Promise<Bill | ApiError> =>
      safeRequest(() =>
        api.put<Bill>("/events/bills", bill).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const updateBillParticipant = useCallback(
    async (bill: UpdateBillParticipants): Promise<AxiosResponse<void> | ApiError> =>
      safeRequest(() =>
        api.put<void>("/events/bills/participants", bill).then((res) => res),
      ),
    [api, safeRequest],
  );

  const deleteBill = useCallback(
    async (billUuid: string): Promise<void | ApiError> =>
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
