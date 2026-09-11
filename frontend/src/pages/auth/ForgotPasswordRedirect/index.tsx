import { isApiError } from "@/util/checkApiResponse";
import styles from "./ForgotPasswordRedirect.module.scss";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { CheckResetPassword } from "@data/Token";
import useTokenService from "@service/useTokenService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import ApiError from "@data/Error";

function ForgotPasswordRedirect() {
  const [first, setFirst] = useState<boolean>(true);
  const { checkResetPassword } = useTokenService();
  const { addNotification } = useAlertsContext();
  const { user, login } = useUserContext();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const sleep = (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const fetchPassword = useCallback(async () => {
    const token = searchParams.get("token");
    if (token && first) {
      setFirst(false);
      await sleep(1000);
      try {
        const response = await checkResetPassword({
          token: token,
        } as CheckResetPassword);
        login({
          uuid: response.uuid,
          firstname: response.firstname,
          role: response.role,
          emailVerified: response.emailVerified,
        });
        navigate("/auth/user");
      } catch (error: ApiError | any) {
        if (isApiError(error)) {
          addNotification({
            title: "Não foi possível redefinir sua senha",
            message: "Seu e-mail expirou, tente novamente enviando outro email.",
            type: MessageType.WARNING,
          });
          navigate("/auth/login");
        }
      }
    }
  }, [searchParams]);

  useEffect(() => {
    if (isUserLogged(user)) {
      navigate("/");
    } else {
      fetchPassword();
    }
  }, []);

  return (
    <div className={styles.body}>
      <h1>Verificando usuário</h1>
      <h2>Não esqueça de alterar a senha</h2>
    </div>
  );
}

export default ForgotPasswordRedirect;
