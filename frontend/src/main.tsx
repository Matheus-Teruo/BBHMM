import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import AlertProvider from "@context/AlertContext/AlertContext";
import UserProvider from "@context/UserContext/UserContext";
import App from "./App";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AlertProvider>
      <UserProvider>
        <App />
      </UserProvider>
    </AlertProvider>
  </StrictMode>,
);
