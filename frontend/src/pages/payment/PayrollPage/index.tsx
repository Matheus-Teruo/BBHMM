import styles from "./PayrollPage.module.scss";
import { ArrowRightSVG } from "@/assets/svg";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import { PaymentDetails } from "@data/Payment";
import { EventUser } from "@data/User";
import useEventService from "@service/useEventService";
import usePaymentService from "@service/usePaymentService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function PayrollPage() {
  const [payroll, setPayroll] = useState<PaymentDetails[]>([]);
  const [users, setUsers] = useState<Record<string, EventUser>>({});
  const { listPayroll } = usePaymentService();
  const { listUserFromEvent } = useEventService();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const { eventUUID } = useParams();

  const fetchUsers = useCallback(async () => {
    if (eventUUID) {
      const usersResponse = await listUserFromEvent(eventUUID);
      if (usersResponse) {
        const usersRecord: Record<string, EventUser> = usersResponse.reduce(
          (acc, user) => {
            acc[user.uuid] = user;
            return acc;
          },
          {} as Record<string, EventUser>,
        );
        setUsers(usersRecord);
      }
    }
  }, [eventUUID, listUserFromEvent]);

  const fetchPayment = useCallback(async () => {
    if (eventUUID) {
      const payroll = await listPayroll(eventUUID);
      if (payroll) {
        setPayroll(payroll);
      }
    }
  }, [listPayroll]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchPayment();
      fetchUsers();
    } else if (isUserUnlogged(user)) {
      navigate("/auth/login");
    }
  }, [user, fetchPayment]);

  return (
    <div className={styles.body}>
      <h2 className={styles.title}>Pagamentos</h2>
      <p className={styles.subTitle}>Items já pagos</p>
      <p className={styles.subTitle}>
        Caso tenha alterações pode existir valor sobressalente
      </p>
      <ul className={styles.list}>
        {payroll.map((pay) => (
          <li key={pay.billUuid}>
            <p>{users[pay.userToPayUuid]?.firstname}</p>
            <ArrowRightSVG />
            <div className={styles.value}>
              <p>Pago R${pay.value.toFixed(2)}</p>
              {!pay.paid && (
                <p>Sobra R${(pay.value - pay.paidValue).toFixed(2)}</p>
              )}
            </div>
            <ArrowRightSVG />
            <p>{users[pay.userToReceiveUuid]?.firstname}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PayrollPage;
