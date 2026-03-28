import styles from "./InviteUserForm.module.scss";
import { CheckSVG } from "@/assets/svg";
import Button from "@/components/util/Button";
import GlassBackground from "@/components/GlassBackground";
import { ButtonHTMLType } from "@/components/util/Button/ButtonHTMLType";
import GeneralInput from "@/components/util/GeneralInput";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { UserInvitation } from "@data/EventInvitation";
import useEventInvitationService from "@service/useEventInvitationService";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { isApiError, mapFieldErrors } from "@/util/checkApiResponse";

function InviteUserForm() {
  const [userfield, setUserfield] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { userEventInvitation } = useEventInvitationService();
  const { eventUUID } = useParams();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    if (eventUUID) {
      e.preventDefault();
      setWaitingFetch(true);
      setTouched(false);
      setMessageError({});
      const invitation = await userEventInvitation({
        userfield: userfield,
        eventUuid: eventUUID,
      } as UserInvitation);
      if (!isApiError(invitation)) {
        addNotification({
          title: "Convite enviado",
          message: `Convite enviado para ${invitation.ownerUser.firstname}`,
          type: MessageType.OK,
        });
        setUserfield("");
        navigate(-1);
      } else if (invitation) {
        const message = invitation;
        if (message.fields) setMessageError(mapFieldErrors(message.fields));
      }
      setTouched(true);
      setWaitingFetch(false);
    } else {
      addNotification({
        title: "Convite não pode ser enviado",
        message: `Convite não pode ser enviado quando nenhum evento foi selecionado`,
        type: MessageType.WARNING,
      });
    }
  };

  return (
    <>
      <div className={styles.modal}>
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
          <div className={styles.footer}>
            <Button type={ButtonHTMLType.Submit} loading={waitingFetch}>
              <p>Convidar</p>
              <CheckSVG />
            </Button>
          </div>
        </form>
      </div>
      <GlassBackground onClick={() => navigate(-1)} />
    </>
  );
}

export default InviteUserForm;
