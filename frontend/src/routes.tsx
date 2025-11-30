import { Route, Routes, useLocation } from "react-router-dom";
import AuthMain from "./pages/auth/AuthMain";
import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signup";
import EventPage from "./pages/event/EventPage";
import BillPage from "./pages/bill/BillPage";
import NotificationManager from "./components/NotificationManager";
import UserProfile from "./pages/auth/UserProfile";
import PaymentPage from "./pages/payment/PaymentPage";
import FooterNav from "./components/FooterNav";
import EventInvitationPage from "./pages/invitation/EventInvitationPage";
import InviteUserForm from "./pages/invitation/InviteUserForm";
import GuestRedirect from "./pages/guest/GuestRedirect";
import GuestInfo from "./pages/guest/GuestInfo";
import CreateGuestForm from "./pages/guest/CreateGuestForm";

function AppRouter() {
  const location = useLocation();

  const state = location.state as { backgroundLocation?: Location } | undefined;
  const backgroundLocation = state?.backgroundLocation;
  return (
    <>
      <NotificationManager />
      <Routes location={backgroundLocation || location}>
        <Route path="/" element={<FooterNav />}>
          <Route path="" element={<EventPage />} />
          <Route path="event">
            <Route path=":eventUUID/bills" element={<BillPage />} />
            <Route path=":eventUUID/payment" element={<PaymentPage />} />
          </Route>
          <Route path="invites" element={<EventInvitationPage />} />
          <Route path="auth" element={<AuthMain />}>
            <Route path="user" element={<UserProfile />} />
            <Route
              path="guest/:guestName/:password"
              element={<GuestRedirect />}
            />
          </Route>
        </Route>
        <Route path="/auth" element={<AuthMain />}>
          <Route path="signup" element={<Signup />} />
          <Route path="login" element={<Login />} />
        </Route>
      </Routes>
      {backgroundLocation && (
        <Routes>
          <Route path="/invites/new/:eventUUID" element={<InviteUserForm />} />
          <Route path="/auth/guest/new" element={<CreateGuestForm />} />
          <Route path="/auth/guest/info" element={<GuestInfo />} />
        </Routes>
      )}
    </>
  );
}

export default AppRouter;
