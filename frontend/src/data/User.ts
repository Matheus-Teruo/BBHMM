import Pix, { UpdatePix } from "./Pix";

export interface SignupUser {
  username: string;
  password: string;
  fullname: string;
  email: string;
  pix?: UpdatePix;
}

export interface LoginUser {
  username: string;
  password: string;
}

export interface UpdateUser {
  uuid: string;
  username?: string;
  password?: string;
  fullname?: string;
  email?: string;
  pix?: UpdatePix;
}

export default interface User {
  uuid: string;
  username: string;
  fullname: string;
  pix: Pix;
}

export interface UserResume {
  uuid: string;
  firstName: string;
}

export interface UserList {
  uuid: string;
  fullname: string;
  pix: Pix;
}
