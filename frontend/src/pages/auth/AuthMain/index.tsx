import { Outlet } from "react-router-dom";
import styles from "./AuthMain.module.scss";
import Logo from "@/assets/image/BBHMM.png";

function AuthMain() {
  return (
    <div className={styles.background}>
      <div className={styles.header}>
        <div className={styles.linkLogo}>
          <img src={Logo} alt="Logo" />
        </div>
      </div>
      <div className={styles.body}>
        <Outlet />
      </div>
      <div className={styles.spaceHolder} />
    </div>
  );
}

export default AuthMain;
