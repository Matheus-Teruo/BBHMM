import styles from "./PaymentHeader.module.scss";
import { Outlet } from "react-router-dom";

function PaymentHeader() {
  return (
    <div className={styles.header}>
      <h2>Dividas</h2>
      <h2>Pagamentos</h2>
      <Outlet />
    </div>
  );
}

export default PaymentHeader;
