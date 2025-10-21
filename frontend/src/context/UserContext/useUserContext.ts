import User from "@data/User";
import { useContext, createContext } from "react";

interface UserContextType {
  user: User | null | "unlogged";
  login: (user: User) => void;
  checkLogged: () => void;
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
