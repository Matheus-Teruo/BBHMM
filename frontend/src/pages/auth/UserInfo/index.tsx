import { FaceSmileSVG } from "@/assets/svg";
import styles from "./UserInfo.module.scss";
import GlassBackground from "@/components/GlassBackground";
import { useLocation, useNavigate } from "react-router-dom";
import { EventUser } from "@data/User";

function UserInfo() {
  const location = useLocation();
  const navigate = useNavigate();

  const { user, backgroundLocation } = location.state as {
    user: EventUser;
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
