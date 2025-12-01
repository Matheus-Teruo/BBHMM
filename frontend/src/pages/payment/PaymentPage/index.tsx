import styles from "./PaymentPage.module.scss";
import { ArrowLeftSVG, ArrowRightSVG } from "@/assets/svg";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Payment from "@data/Payment";
import useEventService from "@service/useEventService";
import usePaymentService from "@service/usePaymentService";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function PaymentPage() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [receivings, setReceivings] = useState<Payment[]>([]);
  const [users, setUsers] = useState<Record<string, string>>({});
  const { getPayment, getReceiving } = usePaymentService();
  const { listUserFromEvent } = useEventService();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const { eventUUID } = useParams();

  const fetchUsers = useCallback(async () => {
    if (eventUUID) {
      const usersResponse = await listUserFromEvent(eventUUID);
      if (usersResponse) {
        const usersRecord: Record<string, string> = usersResponse.reduce(
          (acc, user) => {
            acc[user.uuid] = user.firstname;
            return acc;
          },
          {} as Record<string, string>,
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
  }, [eventUUID, getPayment]);

  function handlePayment(payerUuid: string, receiverUuid: string) {
    navigate("/event/paymnent/new", {
      state: {
        payerUuid: payerUuid,
        receiverUuid: receiverUuid,
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
      <h2 className={styles.title}>Pagamento</h2>
      <ul className={styles.list}>
        {payments.map((payment) => (
          <li
            key={payment.userToReceiveUuid + "-payment"}
            onClick={() =>
              handlePayment(payment.userToPayUuid, payment.userToReceiveUuid)
            }
          >
            <p>{users[payment.userToPayUuid]}</p>
            <ArrowRightSVG />
            <p>Paga R${payment.value.toFixed(2)}</p>
            <ArrowRightSVG />
            <p>{users[payment.userToReceiveUuid]}</p>
          </li>
        ))}
        {receivings.map((receiving) => (
          <li
            key={receiving.userToPayUuid + "-receiving"}
            onClick={() =>
              handlePayment(
                receiving.userToPayUuid,
                receiving.userToReceiveUuid,
              )
            }
          >
            <p>{users[receiving.userToReceiveUuid]}</p>
            <ArrowLeftSVG />
            <p>Paga R${receiving.value.toFixed(2)}</p>
            <ArrowLeftSVG />
            <p>{users[receiving.userToPayUuid]}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default PaymentPage;
