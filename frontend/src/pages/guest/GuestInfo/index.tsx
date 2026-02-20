import styles from "./GuestInfo.module.scss";
import GlassBackground from "@/components/GlassBackground";
import { useLocation, useNavigate } from "react-router-dom";

function GuestInfo() {
  const location = useLocation();
  const navigate = useNavigate();

  const { username, token, backgroundLocation } = location.state as {
    username: string;
    token: string;
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
        <h2>Convidado criado</h2>
        <p>Passe o seguinte link para o convidado acessar o evento</p>
        <p>{`${window.location.origin}/auth/guest/redirect?guestName=${username}&token=${token}`}</p>
      </div>
      <GlassBackground onClick={() => closePopup()} />
    </>
  );
}

export default GuestInfo;
