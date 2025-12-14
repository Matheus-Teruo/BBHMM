import { useEffect, useRef, useState } from "react";
import styles from "./TokenInput.module.scss";
import { InputStatus } from "../InputStatus";

interface GeneralInputProps {
  value: string;
  onChange: (value: string) => void;
  id: string;
  showStatus?: boolean;
  message?: string;
}

const TOKEN_SIZE = 6;

function TokenInput({
  value,
  onChange,
  id,
  showStatus = false,
  message = "",
}: GeneralInputProps) {
  const [codigo, setCodigo] = useState<string[]>(Array(TOKEN_SIZE).fill(""));
  const inputsRef = useRef<(HTMLInputElement | null)[]>([]);
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

  useEffect(() => {
    if (!value) {
      setCodigo(Array(TOKEN_SIZE).fill(""));
      return;
    }

    const digits = value.replace(/\D/g, "").slice(0, TOKEN_SIZE).split("");

    const newCode = Array(TOKEN_SIZE).fill("");
    digits.forEach((d, i) => (newCode[i] = d));

    setCodigo(newCode);
  }, [value]);

  function updateCode(newCode: string[]) {
    setCodigo(newCode);
    onChange(newCode.join(""));
  }

  function handleChange(valor: string, index: number) {
    if (!/^\d?$/.test(valor)) return;

    const newCode = [...codigo];
    newCode[index] = valor;

    updateCode(newCode);
    setStatus(InputStatus.Untouched);

    if (valor && index < TOKEN_SIZE - 1) {
      inputsRef.current[index + 1]?.focus();
    }
  }

  function handleKeyDown(
    e: React.KeyboardEvent<HTMLInputElement>,
    index: number,
  ) {
    if (e.key === "Backspace") {
      if (codigo[index]) {
        const newCode = [...codigo];
        newCode[index] = "";
        updateCode(newCode);
      } else if (index > 0) {
        inputsRef.current[index - 1]?.focus();
      }
    }
  }

  function handlePaste(e: React.ClipboardEvent<HTMLInputElement>) {
    e.preventDefault();

    const text = e.clipboardData
      .getData("text")
      .replace(/\D/g, "")
      .slice(0, TOKEN_SIZE);

    const novoCodigo = Array(TOKEN_SIZE).fill("");

    for (let i = 0; i < text.length; i++) {
      novoCodigo[i] = text[i];
    }

    updateCode(novoCodigo);

    const proximoIndex = Math.min(text.length, TOKEN_SIZE - 1);
    inputsRef.current[proximoIndex]?.focus();
  }

  return (
    <div
      className={`${styles.inputGroup}
      ${
        status === InputStatus.Accepted
          ? styles.unfocOK
          : status === InputStatus.Rejected && styles.unfocNO
      }`}
    >
      {codigo.map((valor, index) => (
        <input
          key={index}
          ref={(el) => {
            inputsRef.current[index] = el;
          }}
          id={id + "-" + index}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={valor}
          onChange={(e) => handleChange(e.target.value, index)}
          onKeyDown={(e) => handleKeyDown(e, index)}
          onPaste={handlePaste}
        />
      ))}
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

export default TokenInput;
