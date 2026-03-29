import { InputStatus } from "../InputStatus";
import styles from "./ColorSelect.module.scss";
import { useEffect, useState } from "react";

interface ColorSelectProps {
  value: string | undefined;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  showStatus?: boolean;
  message?: string;
}

function ColorSelect({
  value,
  onChange,
  showStatus = false,
  message = "",
}: ColorSelectProps) {
  const [status, setStatus] = useState<InputStatus>(InputStatus.Untouched);

  useEffect(() => {
    if (showStatus) {
      if (message === "") {
        setStatus(InputStatus.Accepted);
      } else {
        setStatus(InputStatus.Rejected);
      }
    }
  }, [showStatus, message]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onChange(event);
    setStatus(InputStatus.Untouched);
  };

  return (
    <div
      className={`${styles.base}
      ${
        status === InputStatus.Accepted
          ? styles.unfocOK
          : status === InputStatus.Rejected && styles.unfocNO
      }`}
    >
      <input
        type="color"
        className={styles.colorInput}
        value={value}
        onChange={handleChange}
      />
      {status !== InputStatus.Untouched && message && (
        <span className={styles.messageError}>{message}</span>
      )}
    </div>
  );
}

export default ColorSelect;
