import { regexLeterNumberSpace, regexUuid } from "@/util/regex";
import { CreateEvent, UpdateEvent } from "@data/Event";

interface EvetState {
  uuid: string;
  eventName: string;
  description: string;
}

export type PageAction =
  | { type: "SET_UUID"; payload: string }
  | { type: "SET_EVENT_NAME"; payload: string }
  | { type: "SET_DESCRIPTION"; payload: string }
  | { type: "RESET" };

export const initialEventState: EvetState = {
  uuid: "",
  eventName: "",
  description: "",
};

export function eventReducer(state: EvetState, action: PageAction): EvetState {
  switch (action.type) {
    case "SET_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, uuid: action.payload };
    }
    case "SET_EVENT_NAME": {
      if (!regexLeterNumberSpace.test(action.payload)) {
        return state;
      }
      return { ...state, eventName: action.payload };
    }
    case "SET_DESCRIPTION": {
      if (!regexLeterNumberSpace.test(action.payload)) {
        return state;
      }
      return { ...state, description: action.payload };
    }
    case "RESET":

    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const createEventPayload = (s: EvetState): CreateEvent => {
  const { uuid: _uuid, eventName, description } = s;
  return {
    eventName,
    description,
  } as CreateEvent;
};

export const updateEventPayload = (s: EvetState): UpdateEvent => {
  const payload: Partial<UpdateEvent> = {
    uuid: s.uuid,
    eventName: s.eventName,
    description: s.description,
  };

  Object.keys(payload).forEach((key) => {
    const k = key as keyof typeof payload;
    if (payload[k] === "" || payload[k] === undefined) {
      delete payload[k];
    }
  });
  return payload as UpdateEvent;
};
