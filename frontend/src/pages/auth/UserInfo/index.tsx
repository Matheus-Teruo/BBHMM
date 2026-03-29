import { EditSVG, FaceSmileSVG } from "@/assets/svg";
import styles from "./UserInfo.module.scss";
import GlassBackground from "@/components/GlassBackground";
import { useLocation, useNavigate } from "react-router-dom";
import { EventUser } from "@data/User";
import ColorSelect from "@/components/util/ColorSelect";
import { useEffect, useState } from "react";
import { isApiError, mapFieldErrors } from "@/util/checkApiResponse";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import useEventService from "@service/useEventService";
import { UpdateEventUser } from "@data/Event";
import Button from "@/components/util/Button";
import { isColorDark } from "@/util/colorHelper";

function UserInfo() {
  const [editColor, setEditColor] = useState<boolean>(false);
  const [color, setColor] = useState<string>("#000000");
  const [touched, setTouched] = useState<boolean>(false);
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const { addNotification } = useAlertsContext();
  const { updateEventUser } = useEventService();
  const location = useLocation();
  const navigate = useNavigate();

  const { user, eventUuid, self, backgroundLocation } = location.state as {
    user: EventUser;
    eventUuid: string;
    self: boolean;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

  useEffect(() => {
    if (self && user) {
      setColor(user.color);
    }
  }, [self, user]);

  const handleUpdateSubmit = async () => {
    setWaitingFetch(true);
    setTouched(false);
    const eventUser = await updateEventUser(eventUuid, {
      color: color,
    } as UpdateEventUser);
    if (!isApiError(eventUser)) {
      addNotification({
        title: "Cor atualizada com sucesso",
        message: `cor atualizada para ${eventUser.color}`,
        type: MessageType.OK,
      });
      setEditColor(false);
    } else {
      const message = eventUser;
      if (message.fields) setMessageError(mapFieldErrors(message.fields));
    }
    setTouched(true);
    setWaitingFetch(false);
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

  return (
    <>
      <div className={styles.modal}>
        <h2>Dados do usuário</h2>
        {user && (
          <>
            <div className={styles.field}>
              <p className={styles.title}>Nome </p>
              <div className={styles.value}>
                <FaceSmileSVG />
                <p>{user.firstname}</p>
              </div>
            </div>
            <div className={styles.field}>
              <p className={styles.title}>Nome Completo</p>
              <div className={styles.value}>
                <FaceSmileSVG />
                <p>{user.fullname}</p>
              </div>
            </div>
            {self ? (
              <div className={styles.field}>
                <p className={styles.title}>Cor</p>
                {editColor ? (
                  <div className={`${styles.value} ${styles.colorValue}`}>
                    <ColorSelect
                      value={color}
                      onChange={(e) => setColor(e.target.value)}
                      showStatus={touched}
                      message={messageError["color"]}
                    />
                    <Button
                      className={styles.colorButton}
                      loading={waitingFetch}
                      onClick={() => handleUpdateSubmit()}
                    >
                      <p>Editar</p>
                      <EditSVG />
                    </Button>
                  </div>
                ) : (
                  <div className={`${styles.value} ${styles.colorValue}`}>
                    <p
                      style={{
                        backgroundColor: user.color,
                        color: isColorDark(user.color) ? "white" : "black",
                        padding: "2px 8px",
                        borderRadius: "6px",
                      }}
                    >
                      {user.color}
                    </p>
                    <Button onClick={() => setEditColor(true)}>
                      <EditSVG />
                    </Button>
                  </div>
                )}
              </div>
            ) : (
              <div className={styles.field}>
                <p className={styles.title}>Cor</p>
                <div className={styles.value}>
                  <EditSVG />
                  <p
                    style={{
                      accentColor: user.color,
                    }}
                  >
                    {user.color}
                  </p>
                </div>
              </div>
            )}
            {user.pix ? (
              <>
                <h3>Pix</h3>
                <div className={styles.field}>
                  <p className={styles.title}>Chave Pix</p>
                  <div className={styles.value}>
                    <FaceSmileSVG />
                    <p>{user.pix.pixKey}</p>
                  </div>
                </div>
                <div className={styles.field}>
                  <p className={styles.title}>Nome do banco</p>
                  <div className={styles.value}>
                    <FaceSmileSVG />
                    <p>{user.pix.bankAccount}</p>
                  </div>
                </div>
              </>
            ) : (
              <p className={styles.message}>
                Usuário não possui chave pix cadastrada, terá que pedir
                diretamente
              </p>
            )}
          </>
        )}
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default UserInfo;
