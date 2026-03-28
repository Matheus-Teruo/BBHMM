import { Role, UserResume } from "@data/User";
import React, { useCallback, useEffect, useState } from "react";
import { UserContext } from "./useUserContext";
import useUserService from "@service/useUserService";
import Event from "@data/Event";
import { isApiError } from "@/util/checkApiResponse";

const LOCAL_STORAGE_KEY_EVENT = "selectedEvent";

function UserProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserResume | null | "unlogged">(null);
  const [event, setEvent] = useState<Event | null>(null);
  const { checkUser, logoutUser } = useUserService();

  const login = (user: UserResume) => {
    if (user.role === Role.USER) {
      setEvent(null);
      localStorage.removeItem(LOCAL_STORAGE_KEY_EVENT);
    }
    setUser(user);
  };

  const checkLogged = useCallback(async () => {
    try {
      const logginUser = await checkUser();
      if (!isApiError(logginUser)) {
        setUser(logginUser);
      } else {
        setUser("unlogged");
      }
    } catch (error) {
      setUser("unlogged");
      console.log(error);
    }
  }, [checkUser]);

  const logout = async () => {
    if (user) {
      setUser("unlogged");
      setEvent(null);
      localStorage.removeItem(LOCAL_STORAGE_KEY_EVENT);
      await logoutUser();
    }
  };

  const selectEvent = (event: Event) => {
    setEvent(event);
    localStorage.setItem(LOCAL_STORAGE_KEY_EVENT, JSON.stringify(event));
  };

  useEffect(() => {
    const eventStorage = localStorage.getItem(LOCAL_STORAGE_KEY_EVENT);
    if (eventStorage) {
      const event: Event = JSON.parse(eventStorage);
      setEvent(event);
    }
  }, []);

  useEffect(() => {
    checkLogged();
  }, [checkLogged]);

  return (
    <UserContext.Provider
      value={{
        user,
        event,
        selectEvent,
        login,
        logout,
      }}
    >
      {children}
    </UserContext.Provider>
  );
}

export default UserProvider;
