import {
  regexLeterNumber,
  regexLeterSpace,
  regexPassword,
} from "@/utils/regex";
import { LoginUser, SignupUser } from "@data/User";

type SignupAction =
  | { type: "SET_USERNAME"; payload: string }
  | { type: "SET_FULLNAME"; payload: string }
  | { type: "SET_PASSWORD"; payload: string }
  | { type: "SET_CONFIRM_PASSWORD"; payload: string }
  | { type: "RESET" };

export const initialUserState: SignupUser & { confirmPassword: string } = {
  username: "",
  fullname: "",
  password: "",
  confirmPassword: "",
};

export function userReducer(
  state: SignupUser & { confirmPassword: string },
  action: SignupAction,
): SignupUser & { confirmPassword: string } {
  switch (action.type) {
    case "SET_USERNAME": {
      if (!regexLeterNumber.test(action.payload)) {
        return state;
      }
      return { ...state, username: action.payload };
    }
    case "SET_FULLNAME": {
      if (!regexLeterSpace.test(action.payload)) {
        return state;
      }
      return { ...state, fullname: action.payload };
    }
    case "SET_PASSWORD": {
      if (!regexPassword.test(action.payload)) {
        return state;
      }
      return { ...state, password: action.payload };
    }
    case "SET_CONFIRM_PASSWORD": {
      if (!regexPassword.test(action.payload)) {
        return state;
      }
      return { ...state, confirmPassword: action.payload };
    }
    case "RESET":
      return initialUserState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const signupPayload = (
  state: SignupUser & { confirmPassword: string },
): SignupUser => {
  const { confirmPassword: _confirmPassword, ...signupPayload } = state;
  return signupPayload as SignupUser;
};

export const loginPayload = (
  state: SignupUser & { confirmPassword: string },
): LoginUser => {
  const {
    fullname: _fullname,
    confirmPassword: _confirmPassword,
    ...signupPayload
  } = state;
  return signupPayload as LoginUser;
};
