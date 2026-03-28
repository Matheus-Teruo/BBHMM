import { Message, MessageType } from "@context/AlertContext/useAlertContext";
import ApiError from "@data/Error";
import { AxiosError } from "axios";

export function parseAxiosError(
    error: AxiosError<ApiError>
): Message {
    switch (error.code) {
      case "ERR_NETWORK":
        return {
          title: "Sem conexão",
          message: "Verifique sua internet.",
          type: MessageType.ERROR
        };
      case "ECONNABORTED":
        return {
          title: "Servidor ocupado",
          message: "A requisição demorou muito.",
          type: MessageType.ERROR
        };
    }

    const apiError = error.response?.data;

    if (apiError) {
      return {
        title: apiError.title ?? "Erro",
        message: apiError.message,
        type: MessageType.WARNING,
      };
    }

    return {
      title: "Erro inesperado",
      message: "Algo inesperado aconteceu.",
      type: MessageType.ERROR,
    };
  }