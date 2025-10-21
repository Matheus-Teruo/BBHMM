import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { isAxiosError } from "axios";
import { useCallback } from "react";

export function useApiError() {
  const { addNotification } = useAlertsContext();

  const handleApiError = useCallback(
    (error: unknown) => {
      if (isAxiosError(error)) {
        if (error.code !== "ERR_NETWORK") {
          addNotification({
            title: error.response?.data.error || "Error",
            message: error.response?.data.message || "Something went wrong",
            invalidFields: error.response?.data.invalidFields,
            type: MessageType.WARNING,
          });
        } else {
          addNotification({
            title: "Network Error",
            message: "Please check your internet connection.",
            type: MessageType.ERROR,
          });
        }
      } else {
        console.error("Unknown error:", error);
        addNotification({
          title: "Unexpected Error",
          message: "An unexpected error occurred. Please try again later.",
          type: MessageType.ERROR,
        });
      }
    },
    [addNotification],
  );
  return handleApiError;
}
