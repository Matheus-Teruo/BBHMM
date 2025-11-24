import Pix, { CreatePix, UpdatePix } from "./Pix";

export interface SignupUser {
  username: string;
  password: string;
  fullname: string;
  email: string;
  pix: CreatePix;
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
  email: string;
  pix: Pix;
}

export interface UserResume {
  uuid: string;
  firstName: string;
}

export interface UserList {
  uuid: string;
  firstname: string;
  fullname: string;
  pix: Pix;
}
