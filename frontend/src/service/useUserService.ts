import { useApiError } from "@/axios/useApiError";
import useAxios from "@/axios/useAxios";
import { Message } from "@context/AlertContext/useAlertContext";
import User, {
  LoginUser,
  SignupUser,
  UpdateUser,
  UserResume,
} from "@data/User";
import { AxiosError } from "axios";
import { useCallback } from "react";

const useUserService = () => {
  const api = useAxios();
  const handleApiError = useApiError();

  const safeRequest = useCallback(
    async <T>(fn: () => Promise<T>): Promise<T | Message | null> => {
      try {
        return await fn();
      } catch (error) {
        handleApiError(error);
        if (error instanceof AxiosError) {
          return error.response!.data as Message;
        }
        return null;
      }
    },
    [handleApiError],
  );

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

  const getUser = useCallback(
    async (): Promise<UserResume | null> =>
      api.get<UserResume>("/auth/check").then((res) => res.data),
    [api],
  );

  const logoutUser = useCallback(async (): Promise<void> => {
    await safeRequest(() => api.post<void>("/auth/logout").then(() => {}));
  }, [api, safeRequest]);

  const updateUser = useCallback(
    async (user: UpdateUser): Promise<User | Message | null> =>
      safeRequest(() => api.put<User>("/users", user).then((res) => res.data)),
    [api, safeRequest],
  );

  return {
    signupUser,
    loginUser,
    getUser,
    logoutUser,
    updateUser,
  };
};

export default useUserService;
