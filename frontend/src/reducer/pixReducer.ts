import { regexLetterNumberSpace, regexText } from "@/util/regex";
import Pix, { CreatePix, UpdatePix } from "@data/Pix";

type PixAction =
  | { type: "SET_PIX_KEY"; payload: string }
  | { type: "SET_BANK_ACCOUNT"; payload: string }
  | { type: "SET_PIX"; payload: Pix }
  | { type: "RESET" };

export const initialPixState: CreatePix = {
  pixKey: "",
  bankAccount: "",
};

export function pixReducer(state: CreatePix, action: PixAction): CreatePix {
  switch (action.type) {
    case "SET_PIX_KEY": {
      if (!regexText.test(action.payload)) {
        return state;
      }
      return { ...state, pixKey: action.payload };
    }
    case "SET_BANK_ACCOUNT": {
      if (!regexLetterNumberSpace.test(action.payload)) {
        return state;
      }
      return { ...state, bankAccount: action.payload };
    }
    case "SET_PIX": {
      return {
        pixKey: action.payload.pixKey,
        bankAccount: action.payload.bankAccount,
      };
    }
    case "RESET":
      return initialPixState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const createPixPayload = (state: CreatePix | UpdatePix): CreatePix => {
  const { ...pixPayload } = state;
  return pixPayload as CreatePix;
};

export const updatePixPayload = (state: CreatePix | UpdatePix): UpdatePix => {
  const { ...pixPayload } = state;
  return pixPayload as UpdatePix;
};
