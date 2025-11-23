import styles from "./FooterNav.module.scss";
import { useEffect, useState } from "react";
import { Link, matchPath, Outlet } from "react-router-dom";

function FooterNav() {
  const [eventUUID, setEventUUID] = useState<string>();

  useEffect(() => {
    const valorSalvo = localStorage.getItem("evento");
    if (valorSalvo) {
      setEventUUID(valorSalvo);
    }
  }, []);

  return (
    <>
      <Outlet />
      <div className={styles.footer}>
        {eventUUID && (
          <ul className={styles.subNavigate}>
            <li
              className={`${matchPath({ path: "/event/:eventUUID/bills", end: true }, location.pathname) && styles.selected}`}
            >
              <Link to={`/event/${eventUUID}/bills`}>
                <h3>Contas</h3>
              </Link>
            </li>
            <li
              className={`${matchPath({ path: "/event/:eventUUID/payment", end: true }, location.pathname) && styles.selected}`}
            >
              <Link to={`/event/${eventUUID}/payment`}>
                <h3>Pagamento</h3>
              </Link>
            </li>
          </ul>
        )}
        <ul className={styles.navigate}>
          <li
            className={`${matchPath({ path: "/", end: true }, location.pathname) && styles.selected}`}
          >
            <Link to="/">
              <h3>Eventos</h3>
            </Link>
          </li>
          <li
            className={`${matchPath({ path: "/invites", end: true }, location.pathname) && styles.selected}`}
          >
            <Link to="/invites">
              <h3>Convites</h3>
            </Link>
          </li>
          <li
            className={`${matchPath({ path: "/auth/user", end: true }, location.pathname) && styles.selected}`}
          >
            <Link to="/auth/user">
              <h3>Usuário</h3>
            </Link>
          </li>
        </ul>
      </div>
    </>
  );
}
export default FooterNav;
