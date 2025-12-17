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
  value?: number;
  listPartUuids: string[];
}

export interface UpdateBillParticipants {
  type: string;
  uuid: string;
  partUuid: string;
}

export default interface Bill {
  uuid: string;
  billName: string;
  description: string;
  value: number;
  payerUuid: string;
  participants: Participants[];
}

export interface BillResume {
  uuid: string;
  billName: string;
  payerUuid: string;
  value: number;
  participantsUuid: string[];
}
