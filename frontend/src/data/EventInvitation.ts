import { UserResume } from "./User";

export default interface EventInvitation {
  uuid: string;
  eventName: string;
  description: string;
  eventDate: string;
  accepted: boolean | null;
  invitedUserUuid: string;
  ownerUser: UserResume;
}

export interface UserInvitation {
  userfield: string;
  eventUuid: string;
}

export interface AcceptInvitation {
  uuid: string;
  accept: boolean;
}
