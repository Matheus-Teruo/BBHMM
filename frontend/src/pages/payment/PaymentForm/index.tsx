import styles from "./PaymentForm.module.scss";
import { CheckSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { initialPaymentState, paymentReducer } from "@reducer/paymentReducer";
import usePaymentService from "@service/usePaymentService";
import { useEffect, useReducer, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function PaymentForm() {
  const [state, dispatch] = useReducer(paymentReducer, initialPaymentState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { makePayment } = usePaymentService();
  const { event } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();

  const { payerUuid, receiverUuid } = location.state as {
    payerUuid: string;
    receiverUuid: string;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const payment = await makePayment(state);
    if (payment && !isMessage(payment)) {
      addNotification({
        title: "Conta paga",
        message: `o valor ${state.value} foi pago`,
        type: MessageType.OK,
      });
      dispatch({ type: "RESET" });
      navigate(-1);
    } else if (payment) {
      const message = payment;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  useEffect(() => {
    if (payerUuid) dispatch({ type: "SET_PAYER_UUID", payload: payerUuid });
    if (receiverUuid)
      dispatch({ type: "SET_RECEIVER_UUID", payload: receiverUuid });
  }, [payerUuid, receiverUuid]);

  useEffect(() => {
    if (event) dispatch({ type: "SET_EVENT_UUID", payload: event.uuid });
  }, [event]);

  return (
    <>
      <div className={styles.modal}>
        <h2>Criar Convidado</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            type="number"
            value={state.value}
            onChange={(e) =>
              dispatch({
                type: "SET_VALUE",
                payload: e.target.value,
              })
            }
            id="value"
            placeholder="Valor"
            isRequired
            showStatus={touched}
            message={messageError["name"]}
          />
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Criar</p>
            <CheckSVG />
          </Button>
        </form>
      </div>
      <GlassBackground onClick={() => navigate(-1)} />
    </>
  );
}

export default PaymentForm;
