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
import {
  createEventPayload,
  eventReducer,
  initialEventState,
} from "@reducer/eventReducer";
import useEventService from "@service/useEventService";
import { useReducer, useState } from "react";

interface NewEventProps {
  onChange: () => void;
}

function NewEvent({ onChange }: NewEventProps) {
  const [state, dispatch] = useReducer(eventReducer, initialEventState);
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { createEvent } = useEventService();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const event = await createEvent(createEventPayload(state));
    if (event && !isMessage(event)) {
      addNotification({
        title: "Evento Criado",
        message: `Evento ${state.eventName} criado, adicione mais pessoas`,
        type: MessageType.OK,
      });
      dispatch({ type: "RESET" });
    } else if (event) {
      const message = event;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch(false);
    onChange();
  };

  return (
    <>
      <div>
        <h2>Crie um evento</h2>
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
            message={messageError["username"]}
          />
          <GeneralInput
            value={state.description}
            onChange={(e) =>
              dispatch({ type: "SET_DESCRIPTION", payload: e.target.value })
            }
            id="description"
            placeholder="Descrição do evento"
            isRequired
            showStatus={touched}
            message={messageError["username"]}
          />
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Criar</p>
            <CheckSVG />
          </Button>
        </form>
      </div>
      <GlassBackground onClick={onChange} />
    </>
  );
}

export default NewEvent;
