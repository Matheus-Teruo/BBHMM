import styles from "./Login.module.scss";
import {
  ArrowRightSVG,
  LockPadCloseSVG,
  LockPadOpenSVG,
  UserCheckSVG,
  UserSVG,
  UserXSVG,
} from "@/assets/svg";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import { isApiError, mapFieldErrors } from "@/util/checkApiResponse";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import ApiError from "@data/Error";
import { Role } from "@data/User";
import {
  initialUserState,
  loginPayload,
  userReducer,
} from "@reducer/userReducer";
import useUserService from "@service/useUserService";
import { useEffect, useReducer, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Login() {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { login, user } = useUserContext();
  const { loginUser } = useUserService();
  const navigate = useNavigate();

  useEffect(() => {
    if (isUserLogged(user)) {
      navigate("/");
    }
  }, [user]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setWaitingFetch(true);
      setTouched(false);
      setMessageError({});
      const user = await loginUser(loginPayload(state));
      addNotification({
        title: "Sucesso ao fazer Login",
        message: `Usuário ${state.username} loggado`,
        type: MessageType.OK,
      });
      if (user.role === Role.USER && !user.emailVerified) {
        addNotification({
          title: "Valide seu email",
          message:
            "Sua conta não tem um email validado, Na tela de usuário valide seu email",
          type: MessageType.INFO,
        });
      }
      login(user);
      dispatch({ type: "RESET" });
      navigate("/");
    } catch (error: ApiError | any) {      
      if (isApiError(error)) setMessageError(mapFieldErrors(error.fields));
    } finally {
      setTouched(true);
      setWaitingFetch(false);
    }
  };

  const handleForgotPassword = async () => {
    navigate("/auth/forgot-password", {
      state: {
        backgroundLocation: {
          pathname: location.pathname,
          search: location.search,
        },
      },
    });
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
        <span onClick={() => handleForgotPassword()}>Esqueci minha senha</span>
        <div className={styles.footerSignUp}>
          <p>Não esta cadastrado?</p>
          <Link to="/auth/signup">
            <span>Cadastre-se</span>
          </Link>
        </div>
      </div>
    </>
  );
}

export default Login;
