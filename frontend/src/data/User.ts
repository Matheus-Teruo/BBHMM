import Event from "./Event";
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

export interface ResetGuestToken {
  guestUuid: string;
  eventUuid: string;
}

export interface UpgradeGuestToUser {
  uuid: string;
  username: string;
  password: string;
  email: string;
}

export default interface User {
  uuid: string;
  username: string;
  role: Role;
  fullname: string;
  email: string;
  emailVerified: boolean;
  pix?: Pix;
  imageUrl?: string;
}

export interface UserResume {
  uuid: string;
  firstname: string;
  role: Role;
  emailVerified: boolean;
}

export interface EventUser {
  uuid: string;
  firstname: string;
  fullname: string;
  role: Role;
  pix?: Pix;
  color: string;
  imageUrl?: string;
}

export interface Guest {
  uuid: string;
  username: string;
  event: Event;
}

export interface NewGuest {
  uuid: string;
  username: string;
  fullname: string;
  token: string;
}

export enum Role {
  USER = "usuário",
  GUEST = "convidado",
}
