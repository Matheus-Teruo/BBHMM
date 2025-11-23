import styles from "./UserProfile.module.scss";
import {
  CheckSVG,
  EmailSVG,
  FaceFrownSVG,
  FaceMehSVG,
  FaceSmileSVG,
  LockPadCloseSVG,
  LockPadOpenSVG,
  UserCheckSVG,
  UserSVG,
  UserXSVG,
  XSVG,
} from "@/assets/svg";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import User from "@data/User";
import {
  initialUserState,
  updateUserPayload,
  userReducer,
} from "@reducer/userReducer";
import useUserService from "@service/useUserService";
import { useEffect, useReducer, useState } from "react";

function UserProfile() {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [update, setUpdate] = useState<
    "username" | "fullname" | "password" | "email" | ""
  >("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<
    "username" | "fullname" | "password" | "email" | ""
  >("");
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { user, logout } = useUserContext();
  const { getUser, updateUser } = useUserService();

  useEffect(() => {
    const fetchUser = async () => {
      if (isUserLogged(user)) {
        const userResponse = await getUser(user.uuid);
        if (userResponse) {
          dispatch({ type: "SET_USER", payload: userResponse });
        }
      }
    };
    fetchUser();
  }, [user, getUser]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(update);
    setTouched(false);
    const user = await updateUser(updateUserPayload(state));
    if (user && !isMessage<User>(user)) {
      addNotification({
        title: "Sucesso na atualização",
        message: `Atualização no usuário ${user.username}`,
        type: MessageType.OK,
      });
      dispatch({ type: "RESET" });
      setUpdate("");
    } else if (user) {
      const message = user;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch("");
  };

  const handleUpdate = (
    value: "username" | "fullname" | "password" | "email" | "",
  ) => {
    setTouched(false);
    setUpdate(value);
  };

  const handleLogout = async () => {
    logout();
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <div className={styles.field}>
        <p className={styles.title}>Usuário</p>
        {update !== "username" ? (
          <div
            className={styles.value}
            onClick={() => handleUpdate("username")}
          >
            <UserSVG />
            <p>{state.username}</p>
          </div>
        ) : (
          <div className={styles.update}>
            <AuthInput
              value={state.username}
              onChange={(e) =>
                dispatch({ type: "SET_USERNAME", payload: e.target.value })
              }
              ComponentUntouched={UserSVG}
              ComponentAccepted={UserCheckSVG}
              ComponentRejected={UserXSVG}
              id="username"
              placeholder="Alterar Usuário"
              isRequired
              showStatus={touched}
              message={messageError["username"]}
            />
            <Button
              className={styles.buttonCancel}
              onClick={() => handleUpdate("")}
            >
              <XSVG />
            </Button>
            <Button
              type={ButtonHTMLType.Submit}
              loading={waitingFetch === "username"}
            >
              <CheckSVG />
            </Button>
          </div>
        )}
      </div>
      <div className={styles.field}>
        <p className={styles.title}>Nome Completo</p>
        {update !== "fullname" ? (
          <div
            className={styles.value}
            onClick={() => handleUpdate("fullname")}
          >
            <FaceSmileSVG />
            <p>{state.fullname}</p>
          </div>
        ) : (
          <div className={styles.update}>
            <AuthInput
              value={state.fullname}
              onChange={(e) =>
                dispatch({ type: "SET_FULLNAME", payload: e.target.value })
              }
              ComponentUntouched={FaceMehSVG}
              ComponentAccepted={FaceSmileSVG}
              ComponentRejected={FaceFrownSVG}
              id="fullname"
              placeholder="Alterar Nome Completo"
              isRequired
              showStatus={touched}
              message={messageError["fullname"]}
            />
            <Button
              className={styles.buttonCancel}
              onClick={() => handleUpdate("")}
            >
              <XSVG />
            </Button>
            <Button
              type={ButtonHTMLType.Submit}
              loading={waitingFetch === "fullname"}
            >
              <CheckSVG />
            </Button>
          </div>
        )}
      </div>
      <div className={styles.field}>
        <p className={styles.title}>E-mail</p>
        {update !== "email" ? (
          <div className={styles.value} onClick={() => handleUpdate("email")}>
            <EmailSVG />
            <p>{state.email}</p>
          </div>
        ) : (
          <div className={styles.update}>
            <AuthInput
              value={state.email}
              onChange={(e) =>
                dispatch({ type: "SET_EMAIL", payload: e.target.value })
              }
              ComponentUntouched={EmailSVG}
              ComponentAccepted={EmailSVG}
              ComponentRejected={EmailSVG}
              id="email"
              placeholder="Alterar Email"
              isRequired
              showStatus={touched}
              message={messageError["email"]}
            />
            <Button
              className={styles.buttonCancel}
              onClick={() => handleUpdate("")}
            >
              <XSVG />
            </Button>
            <Button
              type={ButtonHTMLType.Submit}
              loading={waitingFetch === "email"}
            >
              <CheckSVG />
            </Button>
          </div>
        )}
      </div>
      <div className={styles.field}>
        {update !== "password" ? (
          <div className={styles.footer}>
            <Button
              className={styles.buttonFooter}
              onClick={() => handleUpdate("password")}
            >
              Alterar Senha
            </Button>
            <Button className={styles.buttonFooter} onClick={handleLogout}>
              Sair da conta
            </Button>
          </div>
        ) : (
          <>
            <p>Editar Senha</p>
            <div className={styles.valuePassword}>
              <AuthInput
                value={state.password}
                onChange={(e) =>
                  dispatch({ type: "SET_PASSWORD", payload: e.target.value })
                }
                ComponentUntouched={LockPadOpenSVG}
                ComponentAccepted={LockPadCloseSVG}
                ComponentRejected={LockPadOpenSVG}
                id="password"
                placeholder="Nova Senha"
                isSecret
                isRequired
                showStatus={touched}
                message={messageError["password"]}
              />
            </div>
            <div className={styles.valuePassword}>
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
                placeholder="Confirmar Nova Senha"
                isSecret
                isRequired
                onlyStatus
                showStatus={touched}
                message={messageError["password"]}
              />
            </div>
            <div className={styles.updateFooter}>
              <Button
                className={styles.buttonCancel}
                onClick={() => handleUpdate("")}
              >
                <XSVG />
              </Button>
              <Button
                type={ButtonHTMLType.Submit}
                loading={waitingFetch === "password"}
              >
                <CheckSVG />
              </Button>
            </div>
            <Button className={styles.buttonFooter} onClick={handleLogout}>
              Sair da conta
            </Button>
          </>
        )}
      </div>
    </form>
  );
}

export default UserProfile;
