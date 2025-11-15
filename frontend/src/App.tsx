import "./App.module.scss";
import { useUserContext } from "@context/UserContext/useUserContext";
import { useState, useEffect } from "react";
import { isUserLogged } from "./util/checkAuthentication";
import AuthMain from "./components/auth/AuthMain";
import Login from "./components/auth/Login";
import SignUp from "./components/auth/Signup";
import EventPage from "./components/event/EventPage";
import Event from "@data/Event";

enum Logged {
  LOGGED = "logged",
  LOGIN = "login",
  SIGNUP = "sign up",
}

const eventNull: Event = {
  uuid: "",
  eventName: "Sem Evento",
  description: "Nenhum evento selecionado",
  eventDate: "",
};

function App() {
  const [logged, setLogged] = useState<Logged>(Logged.LOGIN);
  const [event, setEvent] = useState<Event>(eventNull);
  const { user } = useUserContext();

  useEffect(() => {
    if (isUserLogged(user)) {
      setLogged(Logged.LOGGED);
    }
  }, [user]);

  return logged === Logged.LOGGED ? (
    <div>
      {event.uuid !== "" ? (
        <div>
          <h2>{event.eventName}</h2>
        </div>
      ) : (
        <>
          <div>HEADER</div>
          <div>
            <EventPage onChange={(event) => setEvent(event)} />
          </div>
        </>
      )}
    </div>
  ) : logged === Logged.LOGIN ? (
    <AuthMain>
      <Login signupRedirect={() => setLogged(Logged.SIGNUP)} />
    </AuthMain>
  ) : (
    <AuthMain>
      <SignUp loginRedirect={() => setLogged(Logged.LOGIN)} />
    </AuthMain>
  );
}

export default App;
