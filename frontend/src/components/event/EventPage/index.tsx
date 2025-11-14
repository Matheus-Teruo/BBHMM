import PageSelect from "@/components/util/PageSelect";
import { isUserLogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Event from "@data/Event";
import { initialPageState, pageReducer } from "@reducer/pageReducer";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useReducer, useState } from "react";

function EventPage() {
  const [events, setEvents] = useState<Event[]>([]);
  const [page, pageDispatch] = useReducer(pageReducer, initialPageState);
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

  return (
    <div>
      <h2>Eventos</h2>
      <ul>
        {events.map((event) => (
          <li>
            <h3>{event.eventName}</h3>
            <p>{event.description}</p>
          </li>
        ))}
      </ul>
      <PageSelect value={page.number} max={page.max} dispatch={pageDispatch} />
    </div>
  );
}

export default EventPage;
