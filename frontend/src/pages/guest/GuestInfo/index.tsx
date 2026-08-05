import styles from "./GuestInfo.module.scss";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { isUserLogged } from "@/util/checkAuthentication";
import { MessageType, useAlertsContext } from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { ResetGuestToken, Role, UserResume } from "@data/User";
import useUserService from "@service/useUserService";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function GuestInfo() {
  const location = useLocation();
  const navigate = useNavigate();
  const { guestUuid, username, token, eventUuid, backgroundLocation } = location.state as {
    guestUuid: string;
    username: string;
    token: string;
    eventUuid: string;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };
  const [userState, setUserState] = useState<UserResume>({
    uuid: "",
    firstname: "",
    role: Role.GUEST,
    emailVerified: false
  });
  const [guestToken, setGuestToken] = useState<string>(token);
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const { getGuestPassword } = useUserService();
  const { addNotification } = useAlertsContext();
  const { user } = useUserContext();

  useEffect(() => {
    if (user && isUserLogged(user)) {
      setUserState(user);
    }
  }, [user]);

  const generateToken = async () => {
    setWaitingFetch(true);
    const request = {guestUuid: guestUuid, eventUuid: eventUuid} as ResetGuestToken;
    const guest = await getGuestPassword(request);
      
    setGuestToken(guest.token)
    setWaitingFetch(false);
  };

  const copyButton = () => {
    navigator.clipboard.writeText(`${window.location.origin}/auth/guest/redirect?guestName=${username}&token=${guestToken}`);
    addNotification({
      title: "Link Copiado",
      message: `O link para login do convidado ${username} foi copiado com sucesso`,
      type: MessageType.INFO,
    });
  }

  const closePopup = () => {
    if (backgroundLocation) {
      navigate(backgroundLocation.pathname, {
        state: {
          ...backgroundLocation,
          onCreated: location.state?.onCreated,
        },
        replace: true,
      });
    } else {
      navigate(-1);
    }
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>Convidado criado</h2>
        <p><strong>Nome: </strong>{username}</p>
        {guestToken ?
          <div className={styles.tokenDiv}>
            <p>Passe o seguinte link para o convidado acessar o evento.</p>
            <div className={styles.tokenFooter}>
              <p>{`${window.location.origin}/auth/guest/redirect?guestName=${username}&token=${guestToken}`}</p>
              <Button
                className={styles.button}
                onClick={() => copyButton()}
              >
                Copiar Link
              </Button>
            </div>
          </div>
        :
          (userState.role === Role.USER &&
            <div className={styles.tokenDiv}>
              <p>Os tokens de acesso não são passados toda vez, para gerar um novo acesso deverá criar um novo token.</p>
              <div className={styles.buttonResetFooter}>
                <Button
                  className={styles.button}
                  onClick={() => generateToken()}
                  loading={waitingFetch}
                >
                  Gerar token de acesso
                </Button>
              </div>
            </div>
        )}
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default GuestInfo;
