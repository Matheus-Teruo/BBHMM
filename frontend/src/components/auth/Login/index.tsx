import {
  ArrowRightSVG,
  LockPadCloseSVG,
  LockPadOpenSVG,
  UserCheckSVG,
  UserSVG,
  UserXSVG,
} from "@/assets/svg";
import styles from "./Login.module.scss";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import User from "@data/User";
import {
  initialUserState,
  loginPayload,
  userReducer,
} from "@reducer/userReducer";
import useUserService from "@service/useUserService";
import { useReducer, useState } from "react";

function Login() {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { login } = useUserContext();
  const { loginVoluntary } = useUserService();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const user = await loginVoluntary(loginPayload(state));
    if (user && !isMessage<User>(user)) {
      addNotification({
        title: "Login Success",
        message: `User ${state.username} logged`,
        type: MessageType.OK,
      });
      login(user);
      dispatch({ type: "RESET" });
    } else if (user) {
      const message = user;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  return (
    <>
      <h1 className={styles.title}>Entrar</h1>
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <AuthInput
            value={state.username}
            onChange={(e) =>
              dispatch({ type: "SET_USERNAME", payload: e.target.value })
            }
            id="username"
            placeholder="Usuário"
            ComponentUntouched={UserSVG}
            ComponentAccepted={UserCheckSVG}
            ComponentRejected={UserXSVG}
            isRequired
            showStatus={touched}
            message={messageError["username"]}
          />
        </div>
        <div className={styles.field}>
          <AuthInput
            value={state.password}
            onChange={(e) =>
              dispatch({ type: "SET_PASSWORD", payload: e.target.value })
            }
            id="password"
            placeholder="Senha"
            ComponentUntouched={LockPadOpenSVG}
            ComponentAccepted={LockPadCloseSVG}
            ComponentRejected={LockPadOpenSVG}
            isSecret
            isRequired
          />
        </div>
        <div className={styles.buttonSpace}>
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Entrar</p>
            <ArrowRightSVG />
          </Button>
        </div>
      </form>
      <div className={styles.footer}>
        <p>Não esta cadastrado?</p>
        <div>
          <span>Cadastre-se</span>
        </div>
      </div>
    </>
  );
}

export default Login;
