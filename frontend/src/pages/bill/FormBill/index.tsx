import styles from "./FormBill.module.scss";
import { CheckSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import { isUserLogged } from "@/util/checkAuthentication";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { BillResume } from "@data/Bills";
import {
  billReducer,
  createBillPayload,
  initialBillState,
  updateBillPayload,
} from "@reducer/billReducer";
import useBillsService from "@service/useBillsService";
import { useEffect, useReducer, useState } from "react";

interface NewBillProps {
  form?: "Create" | "Update";
  initialValue?: BillResume;
  eventUuid?: string;
  onChange: () => void;
}

function FormBill({
  form = "Create",
  initialValue,
  eventUuid,
  onChange,
}: NewBillProps) {
  const [state, dispatch] = useReducer(billReducer, initialBillState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { getBill, createBill, updateBill } = useBillsService();
  const { user } = useUserContext();

  const requestBill = async () => {
    if (initialValue) {
      var bill = await getBill(initialValue.uuid);
      if (bill) dispatch({ type: "SET_BILL", payload: bill });
    }
  };

  useEffect(() => {
    if (isUserLogged(user) && eventUuid) {
      dispatch({ type: "SET_EVENT_UUID", payload: eventUuid });
      dispatch({ type: "SET_PAYER_UUID", payload: user.uuid });
      if (form === "Update") {
        requestBill();
      }
    }
  }, [form]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    if (form === "Create") {
      const event = await createBill(createBillPayload(state));
      if (event && !isMessage(event)) {
        addNotification({
          title: "Evento Criado",
          message: `Evento ${state.name} criado, adicione mais pessoas`,
          type: MessageType.OK,
        });
        dispatch({ type: "RESET" });
        onChange();
      } else if (event) {
        const message = event;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    } else if (form === "Update") {
      const event = await updateBill(updateBillPayload(state));
      if (event && !isMessage(event)) {
        addNotification({
          title: "Evento Editado",
          message: `Evento ${state.name} modificado`,
          type: MessageType.OK,
        });
        dispatch({ type: "RESET" });
        onChange();
      } else if (event) {
        const message = event;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>{`${form === "Create" ? "Cria uma conta" : "Edita conta "}${form === "Update" && initialValue?.billName}`}</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            value={state.name}
            onChange={(e) =>
              dispatch({ type: "SET_NAME", payload: e.target.value })
            }
            id="billName"
            placeholder="Nome da conta"
            isRequired
            showStatus={touched}
            message={messageError["name"]}
          />
          <GeneralInput
            value={state.description}
            onChange={(e) =>
              dispatch({ type: "SET_DESCRIPTION", payload: e.target.value })
            }
            id="billDescription"
            placeholder="Descrição da conta"
            isRequired
            showStatus={touched}
            message={messageError["description"]}
          />
          <GeneralInput
            value={state.value}
            onChange={(e) =>
              dispatch({
                type: "SET_VALUE",
                payload: parseFloat(e.target.value),
              })
            }
            id="billValue"
            placeholder="Valor da conta"
            type="number"
            isRequired
            showStatus={touched}
            message={messageError["value"]}
          />
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>{form === "Create" ? "Criar" : "Atualizar"}</p>
            <CheckSVG />
          </Button>
        </form>
      </div>
      <GlassBackground onClick={onChange} />
    </>
  );
}

export default FormBill;
