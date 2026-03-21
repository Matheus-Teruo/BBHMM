export interface CreateEvent {
  eventName: string;
  description: string;
  eventDate: string;
}

export interface UpdateEvent {
  uuid: string;
  eventName?: string;
  description?: string;
  eventDate?: string;
}

export interface UpdateEventUser {
  color: string;
}

export default interface Event {
  uuid: string;
  eventName: string;
  description: string;
  eventDate: string;
  finished: boolean;
}
