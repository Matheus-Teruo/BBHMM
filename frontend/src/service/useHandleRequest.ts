import { useCallback } from "react";
import { AxiosError, AxiosResponse } from "axios";
import { useApiError } from "@/axios/useApiError";
import { Message } from "@context/AlertContext/useAlertContext";

export const useSafeRequest = () => {
  const handleApiError = useApiError();

  const safeRequest = useCallback(
    async <T>(fn: () => Promise<T>): Promise<T | Message | null> => {
      try {
        return await fn();
      } catch (error) {
        handleApiError(error);
        if (error instanceof AxiosError) {
          return error.response?.data as Message;
        }
        return null;
      }
    },
    [handleApiError],
  );

  const safeRequestWithoutMessage = useCallback(
    async <T>(fn: () => Promise<T>): Promise<T | null> => {
      try {
        return await fn();
      } catch (error) {
        handleApiError(error);
        return null;
      }
    },
    [handleApiError],
  );

  const safeResponse = useCallback(
    async <T>(
      fn: () => Promise<AxiosResponse<T>>,
    ): Promise<AxiosResponse<T> | null> => {
      try {
        return await fn();
      } catch (error) {
        handleApiError(error);
        return null;
      }
    },
    [handleApiError],
  );

  return {
    safeRequest,
    safeRequestWithoutMessage,
    safeResponse,
  };
};
