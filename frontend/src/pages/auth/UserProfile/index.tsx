import styles from "./UserProfile.module.scss";
import {
  AlertCircleSVG,
  BankSVG,
  CheckCircleSVG,
  CheckSVG,
  EmailSVG,
  FaceFrownSVG,
  FaceMehSVG,
  FaceSmileSVG,
  LockPadCloseSVG,
  LockPadOpenSVG,
  PixSVG,
  UserCheckSVG,
  UserSVG,
  UserXSVG,
  XSVG,
} from "@/assets/svg";
import AuthInput from "@/components/util/AuthInput";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import User from "@data/User";
import { initialPixState, pixReducer } from "@reducer/pixReducer";
import {
  initialUserState,
  UpdateUserField,
  updateUserPayload,
  userReducer,
} from "@reducer/userReducer";
import useUserService from "@service/useUserService";
import { useEffect, useReducer, useState } from "react";
import { useNavigate } from "react-router-dom";

function UserProfile() {
  const [state, dispatch] = useReducer(userReducer, initialUserState);
  const [pixState, pixDispatch] = useReducer(pixReducer, initialPixState);
  const [update, setUpdate] = useState<UpdateUserField>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<UpdateUserField>("");
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { user, logout } = useUserContext();
  const { getUser, updateUser } = useUserService();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUser = async () => {
      if (isUserLogged(user)) {
        const userResponse = await getUser(user.uuid);
        if (userResponse) {
          dispatch({ type: "SET_USER", payload: userResponse });
          if (userResponse.pix) {
            pixDispatch({ type: "SET_PIX", payload: userResponse.pix });
          }
        }
      } else if (isUserUnlogged(user)) {
        navigate("/auth/login");
      }
    };
    fetchUser();
  }, [user, getUser]);

  useEffect(() => {
    dispatch({
      type: "SET_PIX",
      payload: pixState,
    });
  }, [pixState]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(update);
    setTouched(false);
    const user = await updateUser(updateUserPayload(state, update));
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

  const handleUpdate = (value: UpdateUserField) => {
    setTouched(false);
    setUpdate(value);
  };

  const handleLogout = async () => {
    logout();
    navigate("/auth/login");
  };

  function handleMail() {
    if (user && user !== "unlogged") {
      navigate("/auth/user/mail-validation", {
        state: {
          userUuid: user.uuid,
          userMail: state.email,
          backgroundLocation: {
            pathname: location.pathname,
            search: location.search,
          },
        },
      });
    }
  }

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
          <>
            <div className={styles.value} onClick={() => handleUpdate("email")}>
              <EmailSVG />
              <p>{state.email}</p>
            </div>
            {state.emailVerified ? (
              <div className={styles.mailConfirmation}>
                <CheckCircleSVG className={styles.mailVerified} />
                <p className={styles.mailVerified}>Email validado</p>
              </div>
            ) : (
              <div
                className={styles.mailConfirmation}
                onClick={() => handleMail()}
              >
                <AlertCircleSVG className={styles.mailUnverified} />
                <p className={styles.mailUnverified}>
                  Confirmação de email pendente
                </p>
              </div>
            )}
          </>
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
      <div className={styles.subForm}>
        <h3 className={styles.subTitle}>Pix</h3>
        <div className={styles.field}>
          <p className={styles.title}>Chave Pix</p>
          {update !== "pix" ? (
            <div className={styles.value} onClick={() => handleUpdate("pix")}>
              <PixSVG />
              <p>{pixState.pixKey}</p>
            </div>
          ) : (
            <div className={styles.update}>
              <AuthInput
                value={pixState.pixKey}
                onChange={(e) =>
                  pixDispatch({ type: "SET_PIX_KEY", payload: e.target.value })
                }
                ComponentUntouched={PixSVG}
                ComponentAccepted={PixSVG}
                ComponentRejected={PixSVG}
                id="pixKey"
                placeholder="Chave Pix"
                isRequired
                showStatus={touched}
                message={messageError["pixKey"]}
              />
            </div>
          )}
        </div>
        <div className={styles.field}>
          <p className={styles.title}>Conta do Banco</p>
          {update !== "pix" ? (
            <div className={styles.value} onClick={() => handleUpdate("pix")}>
              <BankSVG />
              <p>{pixState.bankAccount}</p>
            </div>
          ) : (
            <div className={styles.update}>
              <AuthInput
                value={pixState.bankAccount}
                onChange={(e) =>
                  pixDispatch({
                    type: "SET_BANK_ACCOUNT",
                    payload: e.target.value,
                  })
                }
                ComponentUntouched={BankSVG}
                ComponentAccepted={BankSVG}
                ComponentRejected={BankSVG}
                id="bankAccount"
                placeholder="Nome do Banco"
                isRequired
                showStatus={touched}
                message={messageError["bankAccount"]}
              />
              <Button
                className={styles.buttonCancel}
                onClick={() => handleUpdate("")}
              >
                <XSVG />
              </Button>
              <Button
                type={ButtonHTMLType.Submit}
                loading={waitingFetch === "pix"}
              >
                <CheckSVG />
              </Button>
            </div>
          )}
        </div>
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
            <Button
              className={styles.buttonFooter}
              onClick={() => handleLogout()}
            >
              Sair da conta
            </Button>
          </>
        )}
      </div>
    </form>
  );
}

export default UserProfile;
