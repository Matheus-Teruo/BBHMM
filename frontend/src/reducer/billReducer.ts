import { regexLeterNumberSpace, regexUuid } from "@/util/regex";
import Bill, { CreateBill, UpdateBill } from "@data/Bills";

interface BillState {
  uuid: string;
  name: string;
  description: string;
  value: number;
  eventUuid: string;
  payerUuid: string;
}

export type BillAction =
  | { type: "SET_UUID"; payload: string }
  | { type: "SET_NAME"; payload: string }
  | { type: "SET_DESCRIPTION"; payload: string }
  | { type: "SET_VALUE"; payload: string }
  | { type: "SET_EVENT_UUID"; payload: string }
  | { type: "SET_PAYER_UUID"; payload: string }
  | { type: "SET_BILL"; payload: Bill }
  | { type: "RESET" };

export const initialBillState: BillState = {
  uuid: "",
  name: "",
  description: "",
  value: 0,
  eventUuid: "",
  payerUuid: "",
};

export function billReducer(state: BillState, action: BillAction): BillState {
  switch (action.type) {
    case "SET_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, uuid: action.payload };
    }
    case "SET_NAME": {
      if (!regexLeterNumberSpace.test(action.payload)) {
        return state;
      }
      return { ...state, name: action.payload };
    }
    case "SET_DESCRIPTION": {
      if (!regexLeterNumberSpace.test(action.payload)) {
        return state;
      }
      return { ...state, description: action.payload };
    }
    case "SET_VALUE": {
      const rawValue = action.payload.replace(/[^0-9]/g, "");
      if (!rawValue) return { ...state, value: 0 };
      const numericValue = Math.max(parseFloat(rawValue) / 100, 0);
      return { ...state, value: numericValue };
    }
    case "SET_EVENT_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, eventUuid: action.payload };
    }
    case "SET_PAYER_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, payerUuid: action.payload };
    }
    case "SET_BILL": {
      return {
        ...state,
        uuid: action.payload.uuid,
        name: action.payload.billName,
        description: action.payload.description,
        value: action.payload.value,
      };
    }
    case "RESET":
      return initialBillState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const createBillPayload = (s: BillState): CreateBill => {
  const { uuid: _uuid, name, description, value, eventUuid, payerUuid } = s;
  return {
    name,
    description,
    value,
    eventUuid,
    payerUuid,
  } as CreateBill;
};

export const updateBillPayload = (s: BillState): UpdateBill => {
  const payload: Partial<UpdateBill> = {
    uuid: s.uuid,
    name: s.name,
    description: s.description,
    value: s.value,
  };

  Object.keys(payload).forEach((key) => {
    const k = key as keyof typeof payload;
    if (payload[k] === "" || payload[k] === undefined) {
      delete payload[k];
    }
  });
  return payload as UpdateBill;
};
