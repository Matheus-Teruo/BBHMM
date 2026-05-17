export default interface Payment {
  userToPayUuid: string;
  value: number;
  userToReceiveUuid: string;
}

export interface PaymentDetails {
  billUuid: string;
  userToPayUuid: string;
  value: number;
  paidValue: number;
  paid: boolean;
  userToReceiveUuid: string;
}

export interface DebitTotal {
  debit: boolean;
  value: number;
}

export interface PayBill {
  userToPayUuid: string;
  value: number;
  userToReceiveUuid: string;
  eventUuid: string;
}
