import { CheckSVG } from "@/assets/svg";
import Button from "@/components/util/Button";
// import GlassBackground from "@/components/GlassBackground";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import Event from "@data/Event";
import { UserInvitation } from "@data/EventInvitation";
import useEventInvitationService from "@service/useEventInvitationService";
import { useState } from "react";

interface InviteUserProps {
  event: Event;
  onChange: () => void;
}

function InviteUserForm({ event, onChange }: InviteUserProps) {
  const [userfield, setUserfield] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { userEventInvitation } = useEventInvitationService();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    const invitation = await userEventInvitation({
      userfield: userfield,
      eventUuid: event.uuid,
    } as UserInvitation);
    if (invitation && !isMessage(invitation)) {
      addNotification({
        title: "Convite enviado",
        message: `Convite enviado para ${invitation.ownerUser.firstName}`,
        type: MessageType.OK,
      });
      setUserfield("");
      onChange();
    } else if (invitation) {
      const message = invitation;
      if (message.invalidFields) setMessageError(message.invalidFields);
    }
    setTouched(true);
    setWaitingFetch(false);
  };

  return (
    <>
      <div>
        <h2>Envie um convite</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            value={userfield}
            onChange={(e) => setUserfield(e.target.value)}
            id="userfield"
            placeholder="Nome de usuário ou Nome completo ou Email"
            isRequired
            showStatus={touched}
            message={messageError["userfield"]}
          />
          <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
            <p>Convidar</p>
            <CheckSVG />
          </Button>
        </form>
        <p>{`Convite para o evento ${event.eventName}`}</p>
      </div>
      {/* <GlassBackground onClick={onChange} /> */}
      <div onClick={onChange}>FECHAR</div>
    </>
  );
}

export default InviteUserForm;
