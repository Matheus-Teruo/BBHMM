import { UserResume } from "./User";

export default interface EventInvitation {
  uuid: string;
  eventName: string;
  description: string;
  eventDate: string;
  invitedUserUuid: string;
  ownerUser: UserResume;
}

export interface UserInvitation {
  userUuid: string;
  eventUuid: string;
}

export interface AcceptInvitation {
  uuid: string;
  accept: boolean;
}
