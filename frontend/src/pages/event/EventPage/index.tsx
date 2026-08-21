import styles from "./EventPage.module.scss";
import Button from "@/components/util/Button";
import PageSelect from "@/components/util/PageSelect";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import Event from "@data/Event";
import { initialPageState, pageReducer } from "@reducer/pageReducer";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useReducer, useState } from "react";
import FormEvent from "../FormEvent";
import { EditSVG } from "@/assets/svg";
import { useNavigate } from "react-router-dom";
import SearchFilter from "@/components/util/SearchFilter";
import GeneralInput from "@/components/util/GeneralInput";

function EventPage() {
  const [events, setEvents] = useState<Event[]>([]);
  const [filter, setFilter] = useState<string>("");
  const [dateFilter, setDateFilter] = useState<string>("");
  const [finishedFilter, setFinishedFilter] = useState<boolean>(false);
  const [page, pageDispatch] = useReducer(pageReducer, initialPageState);
  const [eventForm, setEventForm] = useState<null | "Create" | "Update">(null);
  const [selectedEvent, setSelectedEvent] = useState<Event | undefined>();
  const { getEvents } = useEventService();
  const { user, selectEvent } = useUserContext();
  const navigate = useNavigate();

  const fetchEvent = useCallback(async () => {

    const sortDirection = dateFilter ? 'asc' : 'desc';
    const sortParam = `eventDate,${sortDirection}`;

    const eventResponse = await getEvents(
      filter,
      dateFilter,
      finishedFilter,
      page.number,
      20, // Page size
      sortParam
    );
    setEvents(eventResponse.content);
  }, [filter, dateFilter, finishedFilter, page.number, setEvents]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchEvent();
    } else if (isUserUnlogged(user)) {
      navigate("/auth/login");
    }
  }, [user, fetchEvent]);

  const handleFilter = (event: React.ChangeEvent<HTMLInputElement>) => {
    pageDispatch({ type: "SET_PAGE_NUMBER", payload: 0 });
    setEvents([]);
    setFilter(event.target.value);
  };

  function handleUpdateEvent(event: Event) {
    setSelectedEvent(event);
    setEventForm("Update");
  }

  const handleSelectEvent = (event: Event) => {
    selectEvent(event);
    navigate(`/event/${event.uuid}/bills`);
  };

  return (
    <div className={styles.body}>
      <h2 className={styles.title}>Eventos</h2>
      <div className={styles.filterHeader}>
        <SearchFilter value={filter} onChange={handleFilter} />
        <div className={styles.filters}>
          <GeneralInput
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
            id="eventDate"
            placeholder="Dia do evento"
            type="date"
          />
          <div className={styles.finishedFilter}>
            <input
              type="checkbox"
              id="finishedEvent"
              checked={finishedFilter}
              onChange={(e) => setFinishedFilter(e.target.checked)}
            />
            <p>Eventos finalizados</p>
          </div>
        </div>
      </div>
      <div className={styles.header}>
        <Button onClick={() => setEventForm("Create")}>
          Criar Novo Evento
        </Button>
      </div>
      <ul className={styles.list}>
        {events.map((event) => (
          <li
            key={event.uuid}
            className={`${styles.eventCard} ${event.finished ? styles.eventFinished : ""}`}
          >
            <div className={styles.eventDiv} onClick={() => handleSelectEvent(event)}>
              <h3 className={styles.eventTitle}>{event.eventName}</h3>
              <p className={styles.eventDescription}>{event.description}</p>
              <p className={styles.eventDate}>{event.eventDate}</p>
            </div>
            <div className={styles.editArea} onClick={() => handleUpdateEvent(event)}>
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
