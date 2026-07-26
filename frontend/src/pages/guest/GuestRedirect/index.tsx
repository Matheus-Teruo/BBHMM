import { isApiError } from "@/util/checkApiResponse";
import styles from "./GuestRedirect.module.scss";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { LoginUser, Role } from "@data/User";
import useUserService from "@service/useUserService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import ApiError from "@data/Error";

function GuestRedirect() {
  const [first, setFirst] = useState<boolean>(true);
  const { loginGuest } = useUserService();
  const { addNotification } = useAlertsContext();
  const { user, login, selectEvent } = useUserContext();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const sleep = (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const fetchGuest = useCallback(async () => {
    const guestName = searchParams.get("guestName");
    const token = searchParams.get("token");
    if (searchParams && first) {
      setFirst(false);
      await sleep(1500);
      try {
        const guestResponse = await loginGuest({
          username: guestName,
          password: token,
        } as LoginUser);
        login({
          uuid: guestResponse.uuid,
          firstname: guestResponse.username,
          role: Role.GUEST,
          emailVerified: false,
        });
        selectEvent(guestResponse.event);
        navigate(`/event/${guestResponse.event.uuid}/bills`);
      } catch (error: ApiError | any) {
        if (isApiError(error)) {
          addNotification({
            title: "Convidado não existente",
            message:
              "Infelizmente não foi possível entrar, peça outro link de acesso atualizado",
            type: MessageType.WARNING,
          });
          navigate("/auth/login");
        }
      }
    }
  }, [searchParams]);

  useEffect(() => {
    if (isUserLogged(user)) {
      navigate("/");
    } else {
      fetchGuest();
    }
  }, []);

  return (
    <div className={styles.body}>
      <h1>Bem vindo(a)</h1>
      <h2>{searchParams.get("guestName")}</h2>
    </div>
  );
}

export default GuestRedirect;
