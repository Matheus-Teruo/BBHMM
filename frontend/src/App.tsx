import { useUserContext } from "@context/UserContext/useUserContext";
import { useState, useEffect } from "react";
import "./App.css";
import { isUserUnlogged } from "./util/checkAuthentication";
import AuthMain from "./components/auth/AuthMain";
import Login from "./components/auth/Login";

function App() {
  const [logged, setLogged] = useState<boolean>(false)
  const { user } = useUserContext();

  useEffect(() => {
    if (isUserUnlogged(user)) {
      setLogged(true)
    }
  }, [user]);

  return (
    logged ? (
      <div>
        <div>HEADER 2</div>
        <div>
          BODY
          <div></div>
        </div>
      </div>
    ) : (
      <AuthMain> <Login /></AuthMain>
    )
  );
}

export default App;
