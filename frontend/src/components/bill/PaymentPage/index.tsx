import { ArrowLeftSVG, ArrowRightSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import { isUserLogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Event from "@data/Event";
import Payment from "@data/Payment";
import { UserResume } from "@data/User";
import usePaymentService from "@service/usePaymentService";
import { useCallback, useEffect, useState } from "react";

interface PaymentPageProps {
  event: Event;
  usersList: UserResume[];
  onChange: () => void;
}

function PaymentPage({ event, usersList, onChange }: PaymentPageProps) {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [receivings, setReceivings] = useState<Payment[]>([]);
  const [users, setUsers] = useState<Record<string, string>>({});
  const { getPayment, getReceiving } = usePaymentService();
  const { user } = useUserContext();

  const fetchPayment = useCallback(async () => {
    const paymentResponse = await getPayment(event.uuid);
    const receivingResponse = await getReceiving(event.uuid);
    if (paymentResponse) {
      setPayments(paymentResponse);
    }
    if (receivingResponse) {
      setReceivings(receivingResponse);
    }
  }, [event, getPayment]);

  useEffect(() => {
    const usersRecord: Record<string, string> = usersList.reduce(
      (acc, user) => {
        acc[user.uuid] = user.firstName;
        return acc;
      },
      {} as Record<string, string>,
    );
    setUsers(usersRecord);
    if (isUserLogged(user)) {
      fetchPayment();
    }
  }, [user, fetchPayment]);

  return (
    <>
      <div>
        <h2>Pagamento</h2>
        <ul>
          {payments.map((payment) => (
            <li key={payment.userToReceiveUuid + "-payment"}>
              <p>{users[payment.userToPayUuid]}</p>
              <ArrowRightSVG />
              <p>Paga R${payment.value.toFixed(2)}</p>
              <ArrowRightSVG />
              <p>{users[payment.userToReceiveUuid]}</p>
            </li>
          ))}
          {receivings.map((receiving) => (
            <li key={receiving.userToPayUuid + "-receiving"}>
              <p>{users[receiving.userToReceiveUuid]}</p>
              <ArrowLeftSVG />
              <p>Paga R${receiving.value.toFixed(2)}</p>
              <ArrowLeftSVG />
              <p>{users[receiving.userToPayUuid]}</p>
            </li>
          ))}
        </ul>
      </div>
      <GlassBackground onClick={onChange} />
    </>
  );
}

export default PaymentPage;
