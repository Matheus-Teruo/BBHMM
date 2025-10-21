export default interface User {
  uuid: string;
  firstName: string;
}

export interface SignupUser {
  username: string;
  password: string;
  fullname: string;
}

export interface LoginUser {
  username: string;
  password: string;
}
