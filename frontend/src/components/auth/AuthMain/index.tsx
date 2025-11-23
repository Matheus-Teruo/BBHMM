import styles from "./AuthMain.module.scss";
import Logo from "@/assets/image/BBHMM.png";

interface AuthMainProps {
  children: React.ReactNode;
  onExit?: () => void;
}

function AuthMain({ children, onExit = () => undefined }: AuthMainProps) {
  return (
    <div className={styles.background}>
      <div className={styles.header}>
        <div className={styles.linkLogo} onClick={() => onExit()}>
          <img src={Logo} alt="Logo" />
        </div>
      </div>
      <div className={styles.body}>{children}</div>
      <div className={styles.spaceHolder} />
    </div>
  );
}

export default AuthMain;
