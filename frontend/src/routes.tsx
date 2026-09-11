import { Route, Routes, useLocation } from "react-router-dom";
import AuthMain from "./pages/auth/AuthMain";
import Login from "./pages/auth/Login";
import Signup from "./pages/auth/Signup";
import EventPage from "./pages/event/EventPage";
import BillPage from "./pages/bill/BillPage";
import NotificationManager from "./components/NotificationManager";
import UserProfile from "./pages/auth/UserProfile";
import DebtPage from "./pages/payment/DebtPage";
import PayrollPage from "./pages/payment/PayrollPage";
import FooterNav from "./components/FooterNav";
import EventInvitationPage from "./pages/invitation/EventInvitationPage";
import InviteUserForm from "./pages/invitation/InviteUserForm";
import GuestRedirect from "./pages/guest/GuestRedirect";
import GuestInfo from "./pages/guest/GuestInfo";
import CreateGuestForm from "./pages/guest/CreateGuestForm";
import PaymentForm from "./pages/payment/PaymentForm";
import PaymentHeader from "./pages/payment/PaymentHeader";
import UserInfo from "./pages/auth/UserInfo";
import EmailValidation from "./pages/auth/EmailValidation";
import ForgotPasswordForm from "./pages/auth/ForgotPasswordForm";
import ForgotPasswordRedirect from "./pages/auth/ForgotPasswordRedirect";
import UpgradeGuestForm from "./pages/guest/UpgradeGuestForm";

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
            <Route path="" element={<PaymentHeader />}>
              <Route path=":eventUUID/debt" element={<DebtPage />} />
              <Route path=":eventUUID/payroll" element={<PayrollPage />} />
            </Route>
          </Route>
          <Route path="invites" element={<EventInvitationPage />} />
          <Route path="auth" element={<AuthMain />}>
            <Route path="user" element={<UserProfile />} />
          </Route>
        </Route>
        <Route path="/auth" element={<AuthMain />}>
          <Route path="signup" element={<Signup />} />
          <Route path="login" element={<Login />} />
          <Route
            path="user/reset-password"
            element={<ForgotPasswordRedirect />}
          />
          <Route
            path="guest/redirect"
            element={<GuestRedirect />}
          />
        </Route>
      </Routes>
      {backgroundLocation && (
        <Routes>
          <Route path="/event/payment/new" element={<PaymentForm />} />
          <Route path="/invites/new/:eventUUID" element={<InviteUserForm />} />
          <Route path="/auth/guest/new" element={<CreateGuestForm />} />
          <Route path="/auth/guest/info" element={<GuestInfo />} />
          <Route path="/auth/guest/upgrade" element={<UpgradeGuestForm />} />
          <Route path="/auth/user/info" element={<UserInfo />} />
          <Route
            path="/auth/user/mail-validation"
            element={<EmailValidation />}
          />
          <Route
            path="/auth/forgot-password"
            element={<ForgotPasswordForm />}
          />
        </Routes>
      )}
    </>
  );
}

export default AppRouter;
