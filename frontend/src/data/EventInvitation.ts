export default interface EventInvitation {
  uuid: string;
  eventName: string;
  description: string;
  eventDate: string;
  invitedUserUuid: string;
  ownerUser: string;
}

export interface UserInvitation {
  userUuid: string;
  eventUuid: string;
}

export interface AcceptInvitation {
  uuid: string;
  eventUuid: string;
  accept: boolean;
}
