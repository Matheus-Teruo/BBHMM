import LoadingSpinner from "../LoadingSpinner";
import styles from "./Button.module.scss";
import { ButtonHTMLType } from "./ButtonHTMLType";

interface ButtonProps {
  children: React.ReactNode;
  onClick?: () => void;
  type?: ButtonHTMLType;
  className?: string;
  loading?: boolean;
  disabled?: boolean;
}

function Button({
  children,
  onClick,
  type = ButtonHTMLType.Button,
  className,
  loading = false,
  disabled = false,
}: ButtonProps) {
  return (
    <button
      className={`${styles.button} ${className}`}
      type={type}
      onClick={onClick}
      disabled={disabled || loading}
    >
      {!loading ? children : <LoadingSpinner />}
    </button>
  );
}

export default Button;
