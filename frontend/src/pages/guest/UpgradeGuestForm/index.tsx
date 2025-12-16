import { useLocation, useNavigate } from "react-router-dom";
import styles from "./UpgradeGuestForm.module.scss";
import useUserService from "@service/useUserService";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useReducer, useState } from "react";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import {
  initialUserState,
  upgradeGuestPayload,
  userReducer,
} from "@reducer/userReducer";
import { EmailSVG, LockPadCloseSVG, LockPadOpenSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";

function UpgradeGuestForm() {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { upgradeGuestToUser } = useUserService();
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
    const response = await upgradeGuestToUser(upgradeGuestPayload(state));
    if (!isMessage(response)) {
      addNotification({
        title: "Upgrade realizado com sucesso",
        message: "Agora pode editer campos e participar de mais eventos",
        type: MessageType.OK,
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
        <h2>Upgrade de conta</h2>
        <p className={styles.instructions}>
          Informe os seguintes campos para realizar o upgrade
        </p>
        <form onSubmit={handleSubmit}>
          <AuthInput
            value={state.email}
            onChange={(e) =>
              dispatch({
                type: "SET_EMAIL",
                payload: e.target.value,
              })
            }
            ComponentUntouched={EmailSVG}
            ComponentAccepted={EmailSVG}
            ComponentRejected={EmailSVG}
            id="email"
            placeholder="E-mail"
            isRequired
            showStatus={touched}
            message={messageError["email"]}
          />
          <AuthInput
            value={state.password}
            onChange={(e) =>
              dispatch({ type: "SET_PASSWORD", payload: e.target.value })
            }
            ComponentUntouched={LockPadOpenSVG}
            ComponentAccepted={LockPadCloseSVG}
            ComponentRejected={LockPadOpenSVG}
            id="password"
            placeholder="Senha"
            isSecret
            isRequired
            showStatus={touched}
            message={messageError["password"]}
          />
          <AuthInput
            value={state.confirmPassword}
            onChange={(e) =>
              dispatch({
                type: "SET_CONFIRM_PASSWORD",
                payload: e.target.value,
              })
            }
            ComponentUntouched={LockPadOpenSVG}
            ComponentAccepted={LockPadCloseSVG}
            ComponentRejected={LockPadOpenSVG}
            id="confirmPassword"
            placeholder="Confirmar Senha"
            isSecret
            isRequired
            onlyStatus
            showStatus={touched}
            message={messageError["password"]}
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

export default UpgradeGuestForm;
