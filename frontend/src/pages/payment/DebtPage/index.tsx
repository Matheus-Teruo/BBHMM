import styles from "./DebtPage.module.scss";
import { ArrowLeftSVG, ArrowRightSVG } from "@/assets/svg";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Payment from "@data/Payment";
import { EventUser } from "@data/User";
import useEventService from "@service/useEventService";
import usePaymentService from "@service/usePaymentService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function DebtPage() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [receivings, setReceivings] = useState<Payment[]>([]);
  const [users, setUsers] = useState<Record<string, EventUser>>({});
  const { getPayment, getReceiving } = usePaymentService();
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
      const paymentResponse = await getPayment(eventUUID);
      const receivingResponse = await getReceiving(eventUUID);
      if (paymentResponse) {
        setPayments(paymentResponse);
      }
      if (receivingResponse) {
        setReceivings(receivingResponse);
      }
    }
  }, [eventUUID, getPayment, getReceiving]);

  function handlePayment(payer: EventUser, receiver: EventUser, value: number) {
    navigate("/event/paymnent/new", {
      state: {
        payer: payer,
        receiver: receiver,
        value: value.toFixed(2),
        backgroundLocation: {
          pathname: location.pathname,
          search: location.search,
        },
      },
    });
  }

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
      <h2 className={styles.title}>Dividas</h2>
      <p className={styles.subTitle}>
        Selecione um item para realizar um pagamento
      </p>
      <ul className={styles.list}>
        {payments.map((payment) => (
          <li
            key={payment.userToReceiveUuid + "-payment"}
            onClick={() =>
              handlePayment(
                users[payment.userToPayUuid],
                users[payment.userToReceiveUuid],
                payment.value,
              )
            }
          >
            <p>{users[payment.userToPayUuid]?.firstname}</p>
            <ArrowRightSVG />
            <p>Paga R${payment.value.toFixed(2)}</p>
            <ArrowRightSVG />
            <p>{users[payment.userToReceiveUuid]?.firstname}</p>
          </li>
        ))}
        {receivings.map((receiving) => (
          <li
            key={receiving.userToPayUuid + "-receiving"}
            onClick={() =>
              handlePayment(
                users[receiving.userToPayUuid],
                users[receiving.userToReceiveUuid],
                receiving.value,
              )
            }
          >
            <p>{users[receiving.userToReceiveUuid]?.firstname}</p>
            <ArrowLeftSVG />
            <p>Paga R${receiving.value.toFixed(2)}</p>
            <ArrowLeftSVG />
            <p>{users[receiving.userToPayUuid]?.firstname}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default DebtPage;
