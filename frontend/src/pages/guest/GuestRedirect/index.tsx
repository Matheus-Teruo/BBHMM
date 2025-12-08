import styles from "./GuestRedirect.module.scss";
import { isUserLogged } from "@/util/checkAuthentication";
import { isMessage } from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { LoginUser, Role } from "@data/User";
import useUserService from "@service/useUserService";
import { useCallback, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

function GuestRedirect() {
  const { loginGuest } = useUserService();
  const { user, login } = useUserContext();
  const { guestName, password } = useParams();
  const navigate = useNavigate();

  const sleep = (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const fetchGuest = useCallback(async () => {
    if (guestName && password) {
      await sleep(3000);
      const guestResponse = await loginGuest({
        username: guestName,
        password: password,
      } as LoginUser);
      if (guestResponse && !isMessage(guestResponse)) {
        login({
          uuid: guestResponse.uuid,
          firstname: guestResponse.username,
          role: Role.GUEST,
        });
        navigate(`/event/${guestResponse.event.uuid}/bills`);
      }
    }
  }, [guestName, password]);

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
