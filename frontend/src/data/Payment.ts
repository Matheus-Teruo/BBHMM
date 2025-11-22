export default interface Payment {
  userToPayUuid: string;
  value: number;
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
  eventuUuid: string;
}
