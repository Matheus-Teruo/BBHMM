import styles from "./Signup.module.scss";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import {
  CheckSVG,
  FaceFrownSVG,
  FaceMehSVG,
  FaceSmileSVG,
  LockPadCloseSVG,
  LockPadOpenSVG,
  UserCheckSVG,
  UserSVG,
  UserXSVG,
} from "@/assets/svg";
import { useReducer, useState } from "react";
import {
  initialUserState,
  signupPayload,
  userReducer,
} from "@reducer/userReducer";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import useUserService from "@service/useUserService";

function SignUp({ loginRedirect }: { loginRedirect: () => void }) {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { login } = useUserContext();
  const { signupUser } = useUserService();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (state.password == state.confirmPassword) {
      setWaitingFetch(true);
      setTouched(false);
      const user = await signupUser(signupPayload(state));
      if (user && !isMessage(user)) {
        addNotification({
          title: "Signup Success",
          message: `Create user ${user.fullname} and logged`,
          type: MessageType.OK,
        });
        login({
          uuid: user.uuid,
          firstName: user.fullname.split(" ")[0],
        });
        dispatch({ type: "RESET" });
      } else if (isMessage(user)) {
        const message = user;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    } else {
      addNotification({
        title: "Error to Submit",
        message: `password and password confirmation are not iqual`,
        type: MessageType.WARNING,
      });
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  return (
    <>
      <h1 className={styles.title}>Cadastrar</h1>
      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <AuthInput
            value={state.username}
            onChange={(e) =>
              dispatch({ type: "SET_USERNAME", payload: e.target.value })
            }
            ComponentUntouched={UserSVG}
            ComponentAccepted={UserCheckSVG}
            ComponentRejected={UserXSVG}
            id="username"
            placeholder="Usuário"
            isRequired
            showStatus={touched}
            message={messageError["username"]}
          />
        </div>
        <div className={styles.field}>
          <AuthInput
            value={state.fullname}
            onChange={(e) =>
              dispatch({ type: "SET_FULLNAME", payload: e.target.value })
            }
            ComponentUntouched={FaceMehSVG}
            ComponentAccepted={FaceSmileSVG}
            ComponentRejected={FaceFrownSVG}
            id="fullname"
            placeholder="Nome Completo"
            isRequired
            showStatus={touched}
            message={messageError["fullname"]}
          />
        </div>
        <div className={styles.field}>
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
        </div>
        <div className={styles.field}>
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
        </div>
        <div className={styles.field}>
          <AuthInput
            value={state.email}
            onChange={(e) =>
              dispatch({
                type: "SET_EMAIL",
                payload: e.target.value,
              })
            }
            ComponentUntouched={LockPadOpenSVG}
            ComponentAccepted={LockPadCloseSVG}
            ComponentRejected={LockPadOpenSVG}
            id="email"
            placeholder="E-mail"
            isRequired
            showStatus={touched}
            message={messageError["email"]}
          />
        </div>
        <div className={styles.button}>
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Cadastrar</p>
            <CheckSVG />
          </Button>
        </div>
      </form>
      <div className={styles.footer}>
        <p>Já tem conta?</p>
        <div onClick={loginRedirect}>
          <span>Login</span>
        </div>
      </div>
    </>
  );
}

export default SignUp;
