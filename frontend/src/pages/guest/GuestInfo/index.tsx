import styles from "./GuestInfo.module.scss";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { isApiError } from "@/util/checkApiResponse";
import { MessageType, useAlertsContext } from "@context/AlertContext/useAlertContext";
import { ResetGuestToken } from "@data/User";
import useUserService from "@service/useUserService";
import { useState } from "react";
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

  const [guestToken, setGuestToken] = useState<string>(token);
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const { getGuestPassword } = useUserService();
  const { addNotification } = useAlertsContext();

  const generateToken = async () => {
    setWaitingFetch(true);
    const request = {guestUuid: guestUuid, eventUuid: eventUuid} as ResetGuestToken;
    const guest = await getGuestPassword(request);
      
    if (!isApiError(guest)) {
      setGuestToken(guest.token)
    }
    setWaitingFetch(false);
  };

  const copyButton = () => {
    navigator.clipboard.writeText(`${window.location.origin}/auth/guest/redirect?guestName=${username}&token=${token}}`);
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
              <p>{`${window.location.origin}/auth/guest/redirect?guestName=${username}&token=${token}`}</p>
              <Button
                className={styles.button}
                onClick={() => copyButton()}
              >
                Copiar Link
              </Button>
            </div>
          </div>
        :
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
        }
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default GuestInfo;
