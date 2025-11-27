import Event from "@data/Event";
import { UserResume } from "@data/User";
import { useContext, createContext } from "react";

interface UserContextType {
  user: UserResume | null | "unlogged";
  event: Event | null;
  selectEvent: (eventUuid: Event) => void;
  login: (user: UserResume) => void;
  logout: () => void;
}

export const UserContext = createContext<UserContextType | undefined>(
  undefined,
);

export const useUserContext = () => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUserContext must be used within a UserProvider");
  }
  return context;
};
