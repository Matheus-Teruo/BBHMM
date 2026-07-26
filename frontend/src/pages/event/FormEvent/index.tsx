import styles from "./FormEvent.module.scss";
import { CheckSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import { isApiError, mapFieldErrors } from "@/util/checkApiResponse";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import ApiError from "@data/Error";
import Event from "@data/Event";
import {
  createEventPayload,
  eventReducer,
  initialEventState,
  updateEventPayload,
} from "@reducer/eventReducer";
import useEventService from "@service/useEventService";
import { useEffect, useReducer, useState } from "react";

interface NewEventProps {
  form?: "Create" | "Update";
  initialValue?: Event;
  onChange: () => void;
}

function FormEvent({ form = "Create", initialValue, onChange }: NewEventProps) {
  const [state, dispatch] = useReducer(eventReducer, initialEventState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<
    "Create|Update" | "Finish" | ""
  >("");
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { createEvent, updateEvent, finishEvent } = useEventService();

  useEffect(() => {
    if (form === "Update") {
      if (initialValue) dispatch({ type: "SET_EVENT", payload: initialValue });
    }
  }, [form]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setWaitingFetch("Create|Update");
      setTouched(false);
      setMessageError({});
      if (form === "Create") {
        const event = await createEvent(createEventPayload(state));
        addNotification({
          title: "Evento Criado",
          message: `Evento ${event.eventName} criado, adicione mais pessoas`,
          type: MessageType.OK,
        });
      } else if (form === "Update") {
        const event = await updateEvent(updateEventPayload(state));
        addNotification({
          title: "Evento Editado",
          message: `Evento ${event.eventName} editado`,
          type: MessageType.OK,
        });
      }
      dispatch({ type: "RESET" });
      onChange();
    } catch (error: ApiError | any) {
      if (isApiError(error)) {
        if (form === "Create") {
          setMessageError(mapFieldErrors(error.fields));
        } else if (form === "Update") {
          setMessageError(mapFieldErrors(error.fields));
        }
      }
    } finally {
      setTouched(true);
      setWaitingFetch("");
    }
  };

  const handleDelete = async () => {
    try{
      setWaitingFetch("Finish");
      setTouched(false);
      setMessageError({});
      await finishEvent(state.uuid);
      addNotification({
        title: "Evento Finalizado",
        message: `Evento ${state.eventName} finalizado, não podendo ser mais editado`,
        type: MessageType.OK,
      });
      dispatch({ type: "RESET" });
      onChange();
    } catch (error: ApiError | any) {
      if (isApiError(error)) setMessageError(mapFieldErrors(error.fields));
    } finally {
      setTouched(true);
      setWaitingFetch("");
    }
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>{`${form === "Create" ? "Cria um evento" : "Edita evento "}${form === "Update" ? initialValue?.eventName : ""}`}</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            value={state.eventName}
            onChange={(e) =>
              dispatch({ type: "SET_EVENT_NAME", payload: e.target.value })
            }
            id="eventName"
            placeholder="Nome do evento"
            isRequired
            showStatus={touched}
            message={messageError["eventName"]}
          />
          <GeneralInput
            value={state.description}
            onChange={(e) =>
              dispatch({ type: "SET_DESCRIPTION", payload: e.target.value })
            }
            id="eventDescription"
            placeholder="Descrição do evento"
            isRequired
            showStatus={touched}
            message={messageError["description"]}
          />
          <GeneralInput
            value={state.eventDate}
            onChange={(e) =>
              dispatch({ type: "SET_DATE", payload: e.target.value })
            }
            id="eventDate"
            placeholder="Dia do evento"
            type="date"
            isRequired
            showStatus={touched}
            message={messageError["eventDate"]}
          />
          <div className={styles.footer}>
            {form === "Update" ? (
              <Button
                onClick={() => handleDelete()}
                loading={waitingFetch === "Finish"}
              >
                <p>Finalizar</p>
                <CheckSVG />
              </Button>
            ) : (
              <div />
            )}
            <Button
              type={ButtonHTMLType.Submit}
              loading={waitingFetch === "Create|Update"}
            >
              <p>{form === "Create" ? "Criar" : "Atualizar"}</p>
              <CheckSVG />
            </Button>
          </div>
        </form>
      </div>
      <GlassBackground onClick={onChange} />
    </>
  );
}

export default FormEvent;
