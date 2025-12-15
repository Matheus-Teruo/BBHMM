import { useLocation, useNavigate } from "react-router-dom";
import styles from "./EmailValidation.module.scss";
import GlassBackground from "@/components/GlassBackground";
import { useEffect, useState } from "react";
import Button from "@/components/util/Button";
import TokenInput from "@/components/util/TokenInput";
import useTokenService from "@service/useTokenService";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { timerFormater } from "@/util/timerFormater";

const WAIT_TIME = 180;

function EmailValidation() {
  const [token, setToken] = useState<string>("");
  const [timer, setTimer] = useState<number>(0);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { verifyEmail, validateEmail } = useTokenService();
  const { addNotification } = useAlertsContext();
  const location = useLocation();
  const navigate = useNavigate();

  const { userUuid, userMail, backgroundLocation } = location.state as {
    userUuid: string;
    userMail: string;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

  const validateToken = async (value: string) => {
    setWaitingFetch(true);
    setTouched(false);
    if (userUuid) {
      const response = await validateEmail({ token: value });
      if (!isMessage(response)) {
        addNotification({
          title: "Email Confirmado",
          message:
            "Parabens, obrigado por confirmar o email, agora esse email está vinculado a está conta",
          type: MessageType.INFO,
        });
        closePopup();
      } else if (isMessage(response)) {
        const message = response;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  const handleMail = async () => {
    setWaitingFetch(true);
    setTouched(false);
    if (userUuid) {
      const response = await verifyEmail(userUuid);
      if (!isMessage(response)) {
        addNotification({
          title: "Email enviado",
          message: "Confira seu email, possivelmente sua caixa de spam",
          type: MessageType.INFO,
        });
        setTimer(WAIT_TIME);
      } else if (isMessage(response)) {
        const message = response;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  useEffect(() => {
    if (timer <= 0) return;

    const interval = setInterval(() => {
      setTimer((prev) => prev - 1);
    }, 1000);

    return () => clearInterval(interval);
  }, [timer]);

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

  const handleToken = (value: string) => {
    const rawValue = value.replace(/[^0-9]/g, "");
    if (rawValue) {
      setToken(value);
      if (value.length === 6) {
        validateToken(value);
      }
    }
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>Validação do email</h2>
        <div className={styles.tokenAction}>
          <p>Digite o Token que foi enviado para seu email:</p>
          <span>{userMail}</span>
          <TokenInput
            id="tokenValue"
            value={token}
            onChange={(value) => handleToken(value)}
            showStatus={touched}
            message={messageError["tokenValue"]}
          />
        </div>
        <div className={styles.mailAction}>
          <p>Reenvie email, caso precise</p>
          {timer > 0 ? (
            <span>{`Aguarde ${timerFormater(timer)}`}</span>
          ) : (
            <Button onClick={() => handleMail()} loading={waitingFetch}>
              Enviar E-mail
            </Button>
          )}
        </div>
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default EmailValidation;
