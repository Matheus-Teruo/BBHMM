import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import AlertProvider from "@context/AlertContext/AlertContext";
import UserProvider from "@context/UserContext/UserContext";
import AppRouter from "./routes";
import { BrowserRouter } from "react-router-dom";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AlertProvider>
      <UserProvider>
        <BrowserRouter>
          <AppRouter />
        </BrowserRouter>
      </UserProvider>
    </AlertProvider>
  </StrictMode>,
);
