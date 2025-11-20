import Button from "@/components/util/Button";
import PageSelect from "@/components/util/PageSelect";
import { isUserLogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Event from "@data/Event";
import { initialPageState, pageReducer } from "@reducer/pageReducer";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useReducer, useState } from "react";
import FormEvent from "../FormEvent";
import { EditSVG } from "@/assets/svg";

interface EventPageProps {
  onChange: (event: Event) => void;
}

function EventPage({ onChange }: EventPageProps) {
  const [events, setEvents] = useState<Event[]>([]);
  const [page, pageDispatch] = useReducer(pageReducer, initialPageState);
  const [eventForm, setEventForm] = useState<null | "Create" | "Update">(null);
  const [selectedEvent, setSelectedEvent] = useState<Event | undefined>();
  const { getEvents } = useEventService();
  const { user } = useUserContext();

  const fetchEvent = useCallback(async () => {
    const eventResponse = await getEvents(page.number);
    if (eventResponse) {
      setEvents(eventResponse.content);
    }
  }, [page.number, setEvents]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchEvent();
    }
  }, [user, fetchEvent]);

  function updateEvent(event: Event) {
    setSelectedEvent(event);
    setEventForm("Update");
  }

  return (
    <div>
      <h2>Eventos</h2>
      <div>
        <Button onClick={() => setEventForm("Create")}>
          Criar Novo Evento
        </Button>
      </div>
      <ul>
        {events.map((event) => (
          <li key={event.uuid}>
            <h3 onClick={() => onChange(event)}>{event.eventName}</h3>
            <p>{event.description}</p>
            <div onClick={() => updateEvent(event)}>
              <p>Editar</p>
              <EditSVG />
            </div>
          </li>
        ))}
      </ul>
      <PageSelect value={page.number} max={page.max} dispatch={pageDispatch} />
      {eventForm && (
        <FormEvent
          form={eventForm}
          initialValue={selectedEvent}
          onChange={() => {
            fetchEvent();
            return setEventForm(null);
          }}
        />
      )}
    </div>
  );
}

export default EventPage;
