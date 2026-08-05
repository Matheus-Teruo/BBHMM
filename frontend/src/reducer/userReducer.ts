import {
  regexEmail,
  regexLetterNumber,
  regexLetterSpace,
  regexPassword,
} from "@/util/regex";
import { CreatePix, UpdatePix } from "@data/Pix";
import User, {
  LoginUser,
  Role,
  SignupUser,
  UpdateUser,
  UpgradeGuestToUser,
} from "@data/User";
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
  role: Role;
  fullname: string;
  email: string;
  emailVerified: boolean;
  pix: CreatePix | UpdatePix;
  imageUrl?: string;
};

export type UpdateUserField =
  | "username"
  | "fullname"
  | "password"
  | "email"
  | "pix"
  | "";

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
  role: Role.USER,
  email: "",
  emailVerified: true,
  pix: initialPixState,
  imageUrl: undefined
};

export function userReducer(state: UserState, action: UserAction): UserState {
  switch (action.type) {
    case "SET_USERNAME": {
      if (!regexLetterNumber.test(action.payload)) {
        return state;
      }
      return { ...state, username: action.payload };
    }
    case "SET_FULLNAME": {
      if (!regexLetterSpace.test(action.payload)) {
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
        role: action.payload.role,
        fullname: action.payload.fullname,
        email: action.payload.email,
        emailVerified: action.payload.emailVerified,
        pix: action.payload.pix ? action.payload.pix : initialPixState,
        imageUrl: action.payload.imageUrl ? action.payload.imageUrl : undefined,
      };
    }
    case "RESET":
      return initialUserState;
    default:
      throw new Error("Ação desconhecida no reducer");
  }
}

export const signupPayload = (
  state: UserState,
  pixFlag: boolean,
): SignupUser => {
  const { username, password, fullname, email, pix } = state;
  return {
    username,
    password,
    fullname,
    email,
    pix: pixFlag ? createPixPayload(pix) : null,
  } as SignupUser;
};

export const loginPayload = (state: UserState): LoginUser => {
  const { username, password } = state;
  return { username, password } as LoginUser;
};

export const updateUserPayload = (
  state: UserState,
  field: UpdateUserField,
): UpdateUser => {
  const payload: Partial<UpdateUser> = {
    uuid: state.uuid,
  };

  if (!field) {
    return payload as UpdateUser;
  }

  if (field === "pix") {
    payload.pix = updatePixPayload(state.pix);
  } else {
    payload[field] = state[field];
  }

  return payload as UpdateUser;
};

export const upgradeGuestPayload = (state: UserState): UpgradeGuestToUser => {
  const { uuid, username, password, email } = state;
  return { uuid, username, password, email } as UpgradeGuestToUser;
};
