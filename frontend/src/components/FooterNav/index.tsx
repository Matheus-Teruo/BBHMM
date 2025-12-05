import { useUserContext } from "@context/UserContext/useUserContext";
import styles from "./FooterNav.module.scss";
import { Link, matchPath, Outlet } from "react-router-dom";
import { CalendarSVG, InboxSVG, UserSVG } from "@/assets/svg";

function FooterNav() {
  const { event } = useUserContext();

  return (
    <>
      <Outlet />
      <div className={styles.footer}>
        {event && (
          <ul className={styles.subNavigate}>
            <li
              className={`${matchPath({ path: "/event/:eventUUID/bills", end: true }, location.pathname) && styles.selected}`}
            >
              <Link to={`/event/${event.uuid}/bills`}>
                <h3>Contas</h3>
              </Link>
            </li>
            <li
              className={`${matchPath({ path: "/event/:eventUUID/debt", end: true }, location.pathname) && styles.selected}`}
            >
              <Link to={`/event/${event.uuid}/debt`}>
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
              <CalendarSVG />
              <h3>Eventos</h3>
            </Link>
          </li>
          <li
            className={`${matchPath({ path: "/invites", end: true }, location.pathname) && styles.selected}`}
          >
            <Link to="/invites">
              <InboxSVG />
              <h3>Convites</h3>
            </Link>
          </li>
          <li
            className={`${matchPath({ path: "/auth/user", end: true }, location.pathname) && styles.selected}`}
          >
            <Link to="/auth/user">
              <UserSVG />
              <h3>Usuário</h3>
            </Link>
          </li>
        </ul>
      </div>
    </>
  );
}
export default FooterNav;
