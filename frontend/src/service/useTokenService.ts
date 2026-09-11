import useAxios from "@/axios/useAxios";
import { useCallback } from "react";
import { useSafeRequest } from "../axios/useHandleRequest";
import { UserResume } from "@data/User";
import { CheckResetPassword, EmailToken, ResetPassword } from "@data/Token";

const useTokenService = () => {
  const api = useAxios();
  const { safeRequest } = useSafeRequest();

  const resetPassword = useCallback(
    async (payload: ResetPassword): Promise<void> =>
      safeRequest(() =>
        api.post<void>("auth/reset-password", payload).then((res) => res.data),
      ),
    [api, safeRequest],
  );
  const checkResetPassword = useCallback(
    async (payload: CheckResetPassword): Promise<UserResume> =>
      safeRequest(() =>
        api
          .post<UserResume>("auth/check-reset-password", payload)
          .then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const verifyEmail = useCallback(
    async (userUuid: string): Promise<void> =>
      safeRequest(() =>
        api
          .post<void>("/users/verify-email", { userUuid: userUuid })
          .then((res) => res.data),
      ),
    [api, safeRequest],
  );

  const validateEmail = useCallback(
    async (payload: EmailToken): Promise<void> =>
      safeRequest(() =>
        api.post<void>("/users/confirm-email", payload).then((res) => res.data),
      ),
    [api, safeRequest],
  );

  return {
    resetPassword,
    checkResetPassword,
    verifyEmail,
    validateEmail,
  };
};

export default useTokenService;
