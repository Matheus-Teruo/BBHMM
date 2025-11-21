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
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { createEvent, updateEvent } = useEventService();

  useEffect(() => {
    if (form === "Update") {
      if (initialValue) dispatch({ type: "SET_EVENT", payload: initialValue });
    }
  }, [form]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    if (form === "Create") {
      const event = await createEvent(createEventPayload(state));
      if (event && !isMessage(event)) {
        addNotification({
          title: "Evento Criado",
          message: `Evento ${state.eventName} criado, adicione mais pessoas`,
          type: MessageType.OK,
        });
        dispatch({ type: "RESET" });
        onChange();
      } else if (event) {
        const message = event;
        if (message.invalidFields) setMessageError(message.invalidFields);
      }
    } else if (form === "Update") {
      const event = await updateEvent(updateEventPayload(state));
      if (event && !isMessage(event)) {
        addNotification({
          title: "Evento Editado",
          message: `Evento ${state.eventName} criado, adicione mais pessoas`,
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
      <div>
        <h2>{`${form === "Create" ? "Cria um evento" : "Edita evento "}${form === "Update" && initialValue?.eventName}`}</h2>
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

export default FormEvent;
