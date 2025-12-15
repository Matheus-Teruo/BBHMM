import GeneralInput from "@/components/util/GeneralInput";
import styles from "./ForgotPasswordForm.module.scss";
import { useState } from "react";
import Button from "@/components/util/Button";
import useTokenService from "@service/useTokenService";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import { useLocation, useNavigate } from "react-router-dom";
import GlassBackground from "@/components/GlassBackground";

function ForgotPasswordForm() {
  const [email, setEmail] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { resetPassword } = useTokenService();
  const location = useLocation();
  const navigate = useNavigate();

  const { backgroundLocation } = location.state as {
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const response = await resetPassword({ email: email });
    if (!isMessage(response)) {
      addNotification({
        title: "Email enviado",
        message: `Verifique seu email e siga as intruções do email.`,
        type: MessageType.INFO,
      });
      closePopup();
    } else if (isMessage(response)) {
      const message = response;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>Esqueceu a senha</h2>
        <form onSubmit={handleSubmit}>
          <p className={styles.instructions}>Informe o e-mail da conta</p>
          <GeneralInput
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            showStatus={touched}
            message={messageError["email"]}
          />
          <div className={styles.footer}>
            <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
              <p>Confirmar</p>
            </Button>
          </div>
        </form>
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default ForgotPasswordForm;
