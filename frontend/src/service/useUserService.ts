import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import User, {
  CreateGuest,
  Guest,
  LoginUser,
  NewGuest,
  SignupUser,
  UpdateUser,
  UpgradeGuestToUser,
  UserResume,
} from "@data/User";
import { useCallback } from "react";
import { useSafeRequest } from "./useHandleRequest";

const useUserService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const signupUser = useCallback(
    async (user: SignupUser): Promise<User | Message | null> =>
      safeRequest(() =>
        api.post<User>("/auth/signup", user).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const loginUser = useCallback(
    async (user: LoginUser): Promise<UserResume | Message | null> =>
      safeRequest(() =>
        api.post<UserResume>("/auth/login", user).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const checkUser = useCallback(
    async (): Promise<UserResume | null> =>
      api.get<UserResume>("/auth/check").then((res) => res.data),
    [api],
  );

  const logoutUser = useCallback(async (): Promise<void> => {
    await safeRequest(() => api.post<void>("/auth/logout").then(() => {}));
  }, [api, safeRequest]);

  const getUser = useCallback(
    async (userUuid: string): Promise<User | null> =>
      api.get<User>(`/users/${userUuid}`).then((res) => res.data),
    [api],
  );

  const updateUser = useCallback(
    async (user: UpdateUser): Promise<User | Message | null> =>
      safeRequest(() => api.put<User>("/users", user).then((res) => res.data)),
    [api, safeRequest],
  );

  const createGuest = useCallback(
    async (guest: CreateGuest): Promise<NewGuest | Message | null> =>
      safeRequest(() =>
        api.post<NewGuest>("/users/guest", guest).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const loginGuest = useCallback(
    async (guest: LoginUser): Promise<Guest | Message | null> =>
      safeRequest(() =>
        api.post<Guest>("/auth/login/guest", guest).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getGuest = useCallback(
    async (
      guestUuid: string,
      eventUuid: string,
    ): Promise<NewGuest | Message | null> =>
      api
        .get<NewGuest>(`/users/guest/${guestUuid}/event/${eventUuid}`)
        .then((res) => res.data),
    [api],
  );

  const upgradeGuestToUser = useCallback(
    async (guest: UpgradeGuestToUser): Promise<User | Message | null> =>
      safeRequest(() =>
        api.post<User>("/users/guest/upgrade", guest).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  return {
    signupUser,
    loginUser,
    checkUser,
    logoutUser,
    getUser,
    updateUser,
    createGuest,
    loginGuest,
    getGuest,
    upgradeGuestToUser,
  };
};

export default useUserService;
