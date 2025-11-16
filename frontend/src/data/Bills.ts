import Participants from "./Participants";
import { UserResume } from "./User";

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
  billName: string;
  description: string;
  value: number;
  payer: UserResume;
  participants: Participants[];
}

export interface BillResume {
  uuid: string;
  billName: string;
  payerUuid: string;
  value: number;
  participantsUuid: string[];
}
