import Participants from "./Participants";

export interface CreateBill {
  name: string;
  description: string;
  value: number;
  eventUuid: string;
  payerUuid: string;
}

export interface UpdateBill {
  uuid: string;
  name?: string;
  description?: string;
  value?: string;
  listPartUuids: string[];
}

export default interface Bill {
  uuid: string;
  value: number;
  participants: Participants[];
}
