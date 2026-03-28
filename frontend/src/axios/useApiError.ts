import {
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import ApiError from "@data/Error";
import { AxiosError } from "axios";
import { useCallback } from "react";
import { parseAxiosError } from "./parseAxiosError";

export function useApiError() {
  const { addNotification } = useAlertsContext();

  const handleApiError = useCallback(
    (error: AxiosError<ApiError>) => {
      const parsed = parseAxiosError(error);
      addNotification(parsed);
    },
    [addNotification],
  );

  return handleApiError;
}
