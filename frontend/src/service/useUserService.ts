import useAxios from "@/axios/useAxios";
import User, {
  CreateGuest,
  Guest,
  LoginUser,
  NewGuest,
  ResetGuestToken,
  SignupUser,
  UpdateUser,
  UpgradeGuestToUser,
  UserResume,
} from "@data/User";
import { useCallback } from "react";
import { useSafeRequest } from "../axios/useHandleRequest";

const useUserService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const signupUser = useCallback(
    async (user: SignupUser): Promise<User> =>
      safeRequest(() =>
        api.post<User>("/auth/signup", user).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const loginUser = useCallback(
    async (user: LoginUser): Promise<UserResume> =>
      safeRequest(() =>
        api.post<UserResume>("/auth/login", user).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const checkUser = useCallback(
    async (): Promise<UserResume> =>
      api.get<UserResume>("/auth/check").then((res) => res.data),
    [api],
  );

  const logoutUser = useCallback(async (): Promise<void> => {
    await safeRequest(() => api.post<void>("/auth/logout").then(() => {}));
  }, [api, safeRequest]);

  const getUser = useCallback(
    async (userUuid: string): Promise<User> =>
      api.get<User>(`/users/${userUuid}`).then((res) => res.data),
    [api],
  );

  const updateUser = useCallback(
    async (user: UpdateUser): Promise<User> =>
      safeRequest(() => api.put<User>("/users", user).then((res) => res.data)),
    [api, safeRequest],
  );

  const createGuest = useCallback(
    async (guest: CreateGuest): Promise<NewGuest> =>
      safeRequest(() =>
        api.post<NewGuest>("/users/new-guest", guest).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const loginGuest = useCallback(
    async (guest: LoginUser): Promise<Guest> =>
      safeRequest(() =>
        api.post<Guest>("/auth/login/guest", guest).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const getGuest = useCallback(
    async (
      guestUuid: string,
      eventUuid: string,
    ): Promise<NewGuest> =>
      api
        .get<NewGuest>(`/users/guest/${guestUuid}/event/${eventUuid}`)
        .then((res) => res.data),
    [api],
  );

  const getGuestPassword = useCallback(
    async (
      guestData: ResetGuestToken
    ): Promise<NewGuest> =>
      api
        .post<NewGuest>("/users/guest-password", guestData)
        .then((res) => res.data),
    [api],
  );

  const upgradeGuestToUser = useCallback(
    async (guest: UpgradeGuestToUser): Promise<User> =>
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
    getGuestPassword,
    upgradeGuestToUser,
  };
};

export default useUserService;
