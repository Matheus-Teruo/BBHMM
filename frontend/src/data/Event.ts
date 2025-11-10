export interface CreateEvent {
  eventName: string;
  description: string;
}

export interface UpdateEvent {
  uuid: string;
  eventName?: string;
  description?: string;
}

export default interface Event {
  uuid: string;
  eventName: string;
  description: string;
}
