import styles from "./GuestRedirect.module.scss";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { LoginUser, Role } from "@data/User";
import useUserService from "@service/useUserService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function GuestRedirect() {
  const [first, setfirst] = useState<boolean>(true);
  const { loginGuest } = useUserService();
  const { addNotification } = useAlertsContext();
  const { user, login } = useUserContext();
  const { guestName, token } = useParams();
  const navigate = useNavigate();

  const sleep = (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const fetchGuest = useCallback(async () => {
    if (guestName && token && first) {
      setfirst(false);
      await sleep(1500);
      const guestResponse = await loginGuest({
        username: guestName,
        password: token,
      } as LoginUser);
      if (guestResponse && !isMessage(guestResponse)) {
        login({
          uuid: guestResponse.uuid,
          firstname: guestResponse.username,
          role: Role.GUEST,
        });
        navigate(`/event/${guestResponse.event.uuid}/bills`);
      } else if (isMessage(guestResponse)) {
        addNotification({
          title: "Convidado não existente",
          message:
            "Infelizmente não foi possivel entrar, peça outro link de acesso atualizado",
          type: MessageType.WARNING,
        });
        navigate("/auth/login");
      }
    }
  }, [guestName, token]);

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
      <h2>{guestName}</h2>
    </div>
  );
}

export default GuestRedirect;
