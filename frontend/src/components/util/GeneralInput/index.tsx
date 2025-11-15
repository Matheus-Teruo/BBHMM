import { useEffect, useState } from "react";
import styles from "./GeneralInput.module.scss";
import { InputStatus } from "../InputStatus";

interface GeneralInputProps {
  value: string | number | undefined;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
  id: string;
  type?: string;
  placeholder?: string;
  maxLength?: number;
  isRequired?: boolean;
  disabled?: boolean;
  showStatus?: boolean;
  message?: string;
}

function GeneralInput({
  value,
  onChange,
  id,
  type = "text",
  placeholder = "",
  maxLength,
  isRequired = false,
  disabled = false,
  showStatus = false,
  message = "",
}: GeneralInputProps) {
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
      className={`${styles.inputGroup}
      ${
        status === InputStatus.Accepted
          ? styles.unfocOK
          : status === InputStatus.Rejected && styles.unfocNO
      }`}
    >
      <input
        value={value}
        onChange={handleChange}
        placeholder={placeholder}
        id={id}
        name={id}
        type={type}
        inputMode={type === "number" ? "numeric" : undefined}
        pattern={type === "number" ? "\\d*" : undefined}
        required={isRequired}
        disabled={disabled}
        maxLength={maxLength}
      />
      {status !== InputStatus.Untouched && message && (
        <span className={styles.messageError}>{message}</span>
      )}
      {/* <ul>
        {status === InputStatus.Rejected &&
          messages.map((message) => <li>{message}</li>)}
      </ul> */}
    </div>
  );
}

export default GeneralInput;
