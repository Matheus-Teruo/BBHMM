import styles from "./AuthMain.module.scss";
import Logo from "@/assets/image/BBHMM.png";

function AuthMain({ children }: { children: React.ReactNode }) {
  return (
    <div className={styles.background}>
      <div className={styles.header}>
        <div className={styles.linkLogo}>
          <img
            src={Logo}
            alt="Logo: imagem circular com um rosto de raposa no meio"
          />
        </div>
      </div>
      <div className={styles.body}>{children}</div>
      <div className={styles.spaceHolder} />
    </div>
  );
}

export default AuthMain;
