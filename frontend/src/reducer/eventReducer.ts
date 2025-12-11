import { regexDate, regexText, regexUuid } from "@/util/regex";
import Event, { CreateEvent, UpdateEvent } from "@data/Event";

interface EventState {
  uuid: string;
  eventName: string;
  description: string;
  eventDate: string;
}

export type EventAction =
  | { type: "SET_UUID"; payload: string }
  | { type: "SET_EVENT_NAME"; payload: string }
  | { type: "SET_DESCRIPTION"; payload: string }
  | { type: "SET_DATE"; payload: string }
  | { type: "SET_EVENT"; payload: Event }
  | { type: "RESET" };

export const initialEventState: EventState = {
  uuid: "",
  eventName: "",
  description: "",
  eventDate: "",
};

export function eventReducer(
  state: EventState,
  action: EventAction,
): EventState {
  switch (action.type) {
    case "SET_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, uuid: action.payload };
    }
    case "SET_EVENT_NAME": {
      if (!regexText.test(action.payload)) {
        return state;
      }
      return { ...state, eventName: action.payload };
    }
    case "SET_DESCRIPTION": {
      if (!regexText.test(action.payload)) {
        return state;
      }
      return { ...state, description: action.payload };
    }
    case "SET_DATE": {
      if (!regexDate.test(action.payload)) {
        return state;
      }
      return { ...state, eventDate: action.payload };
    }
    case "SET_EVENT": {
      return action.payload;
    }
    case "RESET":
      return initialEventState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const createEventPayload = (s: EventState): CreateEvent => {
  const { uuid: _uuid, eventName, description, eventDate } = s;
  return {
    eventName,
    description,
    eventDate,
  } as CreateEvent;
};

export const updateEventPayload = (s: EventState): UpdateEvent => {
  const payload: Partial<UpdateEvent> = {
    uuid: s.uuid,
    eventName: s.eventName,
    description: s.description,
    eventDate: s.eventDate,
  };

  Object.keys(payload).forEach((key) => {
    const k = key as keyof typeof payload;
    if (payload[k] === "" || payload[k] === undefined) {
      delete payload[k];
    }
  });
  return payload as UpdateEvent;
};
