import styles from "./CreateGuestForm.module.scss";
import { CheckSVG } from "@/assets/svg";
import GlassBackground from "@/components/GlassBackground";
import Button from "@/components/util/Button";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import { regexLeterNumber } from "@/util/regex";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { CreateGuest } from "@data/User";
import useUserService from "@service/useUserService";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

interface CreateGuestFormProps {
  eventUuid?: string;
  onChange: () => void;
}

function CreateGuestForm({ eventUuid, onChange }: CreateGuestFormProps) {
  const [guestName, setGuestName] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { createGuest } = useUserService();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const guest = await createGuest({
      guestName: guestName,
      eventUuid: eventUuid,
    } as CreateGuest);
    if (guest && !isMessage(guest)) {
      addNotification({
        title: "Convidado criado",
        message: `Convidado ${guestName} criado`,
        type: MessageType.OK,
      });
      setGuestName("");
      navigate(`/user/guest/info`, {
        state: {
          username: guest.username,
          token: guest.token,
          backgroundLocation: {
            pathname: location.pathname,
            search: location.search,
          },
        },
      });
      onChange();
    } else if (guest) {
      const message = guest;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }

    setTouched(true);
    setWaitingFetch(false);
  };

  const handleGuestName = (value: string) => {
    if (regexLeterNumber.test(value)) setGuestName(value);
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
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Criar</p>
            <CheckSVG />
          </Button>
        </form>
      </div>
      <GlassBackground onClick={onChange} />
    </>
  );
}

export default CreateGuestForm;
