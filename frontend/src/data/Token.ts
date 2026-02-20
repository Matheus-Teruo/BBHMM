export interface ResetPassword {
  email: string;
}

export interface CheckResetPassword {
  token: string;
}

export interface EmailToken {
  token: string;
}
