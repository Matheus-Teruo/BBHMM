export interface ResetPassword {
  email: string;
}

export interface CheckResetPassword {
  email: string;
  token: string;
}

export interface EmailToken {
  token: string;
}
