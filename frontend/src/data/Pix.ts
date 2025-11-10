export interface CreatePix {
  pixKey: string;
  bankAccount: string;
}

export interface UpdatePix {
  pixKey?: string;
  bankAccount?: string;
}

export default interface Pix {
  uuid: string;
  pixKey: string;
  bankAccount: string;
}
