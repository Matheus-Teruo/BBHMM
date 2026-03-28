import { useCallback } from "react";
import axios from "axios";
import { useApiError } from "@/axios/useApiError";
import ApiError from "@data/Error";

export const useSafeRequest = () => {
  const handleApiError = useApiError();

  const safeRequest = useCallback(
    async <T>(request: () => Promise<T>): Promise<T | ApiError> => {
      try {
        return await request();
      } catch (error) {
        if (axios.isAxiosError<ApiError>(error)) {
          handleApiError(error);
          if (error.response?.data) return error.response.data;
        }

        throw error;
      }
    },
    [handleApiError],
  );


  return {
    safeRequest
  };
};
