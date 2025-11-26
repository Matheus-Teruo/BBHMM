import {
  regexEmail,
  regexLeterNumber,
  regexLeterSpace,
  regexPassword,
} from "@/util/regex";
import { CreatePix, UpdatePix } from "@data/Pix";
import User, { LoginUser, SignupUser, UpdateUser } from "@data/User";
import {
  createPixPayload,
  initialPixState,
  updatePixPayload,
} from "./pixReducer";

type UserState = {
  uuid: string;
  username: string;
  password: string;
  confirmPassword: string;
  fullname: string;
  email: string;
  pix: CreatePix | UpdatePix;
};

type UserAction =
  | { type: "SET_USERNAME"; payload: string }
  | { type: "SET_FULLNAME"; payload: string }
  | { type: "SET_PASSWORD"; payload: string }
  | { type: "SET_CONFIRM_PASSWORD"; payload: string }
  | { type: "SET_EMAIL"; payload: string }
  | { type: "SET_PIX"; payload: CreatePix | UpdatePix }
  | { type: "SET_USER"; payload: User }
  | { type: "RESET" };

export const initialUserState: UserState = {
  uuid: "",
  username: "",
  fullname: "",
  password: "",
  confirmPassword: "",
  email: "",
  pix: initialPixState,
};

export function userReducer(state: UserState, action: UserAction): UserState {
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
    case "SET_EMAIL": {
      if (!regexEmail.test(action.payload)) {
        return state;
      }
      return { ...state, email: action.payload };
    }
    case "SET_PIX": {
      return { ...state, pix: action.payload };
    }
    case "SET_USER": {
      return {
        ...state,
        uuid: action.payload.uuid,
        username: action.payload.username,
        fullname: action.payload.fullname,
        email: action.payload.email,
        pix: action.payload.pix ? action.payload.pix : initialPixState,
      };
    }
    case "RESET":
      return initialUserState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const signupPayload = (s: UserState, pixFlag: boolean): SignupUser => {
  const { username, password, fullname, email, pix } = s;
  return {
    username,
    password,
    fullname,
    email,
    pix: pixFlag ? createPixPayload(pix) : null,
  } as SignupUser;
};

export const loginPayload = (s: UserState): LoginUser => {
  const { username, password } = s;
  return { username, password } as LoginUser;
};

export const updateUserPayload = (s: UserState): UpdateUser => {
  const payload: Partial<UpdateUser> = {
    uuid: s.uuid,
    username: s.username,
    password: s.password,
    fullname: s.fullname,
    email: s.email,
    pix: updatePixPayload(s.pix),
  };

  Object.keys(payload).forEach((key) => {
    const k = key as keyof typeof payload;
    if (payload[k] === "" || payload[k] === undefined) {
      delete payload[k];
    }
  });

  return payload as UpdateUser;
};
