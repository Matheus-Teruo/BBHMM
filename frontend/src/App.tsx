import "./App.module.scss";
import { useUserContext } from "@context/UserContext/useUserContext";
import { useState, useEffect } from "react";
import { isUserLogged } from "./util/checkAuthentication";
import AuthMain from "./components/auth/AuthMain";
import Login from "./components/auth/Login";
import SignUp from "./components/auth/Signup";

enum Logged {
  LOGGED = "logged",
  LOGIN = "login",
  SIGNUP = "sign up",
}

function App() {
  const [logged, setLogged] = useState<Logged>(Logged.LOGIN);
  const { user } = useUserContext();

  useEffect(() => {
    if (isUserLogged(user)) {
      setLogged(Logged.LOGGED);
    }
  }, [user]);

  return logged === Logged.LOGGED ? (
    <div>
      <div>HEADER 2</div>
      <div>
        BODY
        <div></div>
      </div>
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
