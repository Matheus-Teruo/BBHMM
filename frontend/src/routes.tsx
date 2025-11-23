import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthMain from "./pages/auth/AuthMain";
import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signup";
import EventPage from "./pages/event/EventPage";
import BillPage from "./pages/bill/BillPage";
import NotificationManager from "./components/NotificationManager";
import UserProfile from "./pages/auth/UserProfile";
import PaymentPage from "./pages/payment/PaymentPage";

function AppRouter() {
  return (
    <BrowserRouter>
      <NotificationManager />
      <Routes>
        <Route path="/events" element={<EventPage />} />
        <Route path="/event">
          <Route path="/:eventUUID/bills" element={<BillPage />} />
          <Route path="/:eventUUID/payment" element={<PaymentPage />} />
        </Route>
        <Route path="/auth" element={<AuthMain />}>
          <Route path="signup" element={<Signup />} />
          <Route path="login" element={<Login />} />
          <Route path="user" element={<UserProfile />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;
