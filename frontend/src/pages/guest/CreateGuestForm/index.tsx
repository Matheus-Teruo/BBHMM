import styles from "./CreateGuestForm.module.scss";
import { CheckSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import { isApiError, mapFieldErrors } from "@/util/checkApiResponse";
import { regexLetterNumber } from "@/util/regex";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import { CreateGuest } from "@data/User";
import useUserService from "@service/useUserService";
import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function CreateGuestForm() {
  const [guestName, setGuestName] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { createGuest } = useUserService();
  const { event } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();

  const { backgroundLocation } = location.state as {
    username: string;
    token: string;
    backgroundLocation?: {
      pathname: string;
      search: string;
    };
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const guest = await createGuest({
      guestName: guestName,
      eventUuid: event?.uuid,
    } as CreateGuest);
    if (!isApiError(guest)) {
      addNotification({
        title: "Convidado criado",
        message: `Convidado ${guestName} criado`,
        type: MessageType.OK,
      });
      setGuestName("");
      navigate(`/auth/guest/info`, {
        state: {
          username: guest.username,
          token: guest.token,
          backgroundLocation: backgroundLocation,
          onCreated: true,
        },
      });
    } else {
      const message = guest;
      if (message.fields) setMessageError(mapFieldErrors(message.fields));
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  const handleGuestName = (value: string) => {
    if (regexLetterNumber.test(value)) setGuestName(value);
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>Criar Convidado</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            value={guestName}
            onChange={(e) => handleGuestName(e.target.value)}
            id="guestName"
            placeholder="Nome do convidado"
            isRequired
            showStatus={touched}
            message={messageError["name"]}
          />
          <div className={styles.footer}>
            <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
              <p>Criar</p>
              <CheckSVG />
            </Button>
          </div>
        </form>
      </div>
      <GlassBackground onClick={() => navigate(-1)} />
    </>
  );
}

export default CreateGuestForm;
