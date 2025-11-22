import {
  AlertCircleSVG,
  CheckCircleSVG,
  InfoSVG,
  XCircleSVG,
  XSVG,
} from "@/assets/svg";
import styles from "./MessageAlert.module.scss";
import { ReactElement } from "react";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import Button from "@/components/util/Button";

const AlertSVG: Record<MessageType, { component: ReactElement }> = {
  [MessageType.OK]: { component: <CheckCircleSVG size={20} /> },
  [MessageType.INFO]: { component: <InfoSVG size={20} /> },
  [MessageType.WARNING]: { component: <AlertCircleSVG size={20} /> },
  [MessageType.ERROR]: { component: <XCircleSVG size={20} /> },
};

type MessageAlertProps = {
  id: number;
  title: string;
  message: string;
  fields?: Record<string, string>;
  type: MessageType;
};

function MessageAlert({ id, title, message, fields, type }: MessageAlertProps) {
  const { removeNotification } = useAlertsContext();
  return (
    <div className={`${styles.frame} ${styles[type]}`}>
      <div className={styles.header}>
        <h3 className={styles.title}>
          {AlertSVG[type].component}
          {title}
        </h3>
        <Button className={styles.close} onClick={() => removeNotification(id)}>
          <XSVG />
        </Button>
      </div>
      <div className={styles.body}>
        <p>{message}</p>
        {fields && (
          <>
            <p>Espefíficações:</p>
            <ul>
              {Object.entries(fields).map(([field, message]) => (
                <li key={field}>
                  <p>{message}</p>
                </li>
              ))}
            </ul>
          </>
        )}
      </div>
    </div>
  );
}

export default MessageAlert;
