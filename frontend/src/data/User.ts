import Pix, { CreatePix, UpdatePix } from "./Pix";

export interface SignupUser {
  username: string;
  password: string;
  fullname: string;
  email: string;
  pix?: CreatePix;
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

export interface CreateGuest {
  guestName: string;
  eventUuid: string;
}

export interface UpgradeGuestToUser {
  uuid: string;
  email: string;
}

export default interface User {
  uuid: string;
  username: string;
  role: string;
  fullname: string;
  email: string;
  pix?: Pix;
}

export interface UserResume {
  uuid: string;
  firstName: string;
  role: string;
}

export interface UserList {
  uuid: string;
  firstname: string;
  fullname: string;
  role: string;
  pix?: Pix;
}
