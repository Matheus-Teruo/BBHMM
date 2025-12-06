import { useUserContext } from "@context/UserContext/useUserContext";
import styles from "./PaymentHeader.module.scss";
import { Link, matchPath, Outlet } from "react-router-dom";

function PaymentHeader() {
  const { event } = useUserContext();

  return (
    <>
      <div className={styles.header}>
        {event && (
          <>
            <Link to={`/event/${event.uuid}/debt`}>
              <h2
                className={`${matchPath({ path: "/event/:eventUUID/debt", end: true }, location.pathname) && styles.selected}`}
              >
                Dividas
              </h2>
            </Link>
            <Link to={`/event/${event.uuid}/payroll`}>
              <h2
                className={`${matchPath({ path: "/event/:eventUUID/payroll", end: true }, location.pathname) && styles.selected}`}
              >
                Pagamentos
              </h2>
            </Link>
          </>
        )}
      </div>
      <Outlet />
    </>
  );
}

export default PaymentHeader;
