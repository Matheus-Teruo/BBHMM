import { regexUuid } from "@/util/regex";
import { PayBill } from "@data/Payment";

type PaymentAction =
  | { type: "SET_PAYER_UUID"; payload: string }
  | { type: "SET_VALUE"; payload: string }
  | { type: "SET_RECEIVER_UUID"; payload: string }
  | { type: "SET_EVENT_UUID"; payload: string }
  | { type: "RESET" };

export const initialPaymentState: PayBill = {
  userToPayUuid: "",
  value: 0,
  userToReceiveUuid: "",
  eventUuid: "",
};

export function paymentReducer(state: PayBill, action: PaymentAction): PayBill {
  switch (action.type) {
    case "SET_PAYER_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, userToPayUuid: action.payload };
    }
    case "SET_VALUE": {
      const rawValue = action.payload.replace(/[^0-9]/g, "");
      if (!rawValue) return { ...state, value: 0 };
      const numericValue = Math.max(parseFloat(rawValue) / 100, 0);
      return { ...state, value: numericValue };
    }
    case "SET_RECEIVER_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, userToReceiveUuid: action.payload };
    }
    case "SET_EVENT_UUID": {
      if (!regexUuid.test(action.payload)) {
        return state;
      }
      return { ...state, eventUuid: action.payload };
    }
    case "RESET":
      return initialPaymentState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}
