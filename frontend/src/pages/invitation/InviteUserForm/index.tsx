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
import ApiError from "@data/Error";

function InviteUserForm() {
  const [userField, setUserField] = useState<string>("");
  const [messageError, setMessageError] = useState<Record<string, string>>({});
  const [waitingFetch, setWaitingFetch] = useState<boolean>(false);
  const [touched, setTouched] = useState<boolean>(false);
  const { addNotification } = useAlertsContext();
  const { userEventInvitation } = useEventInvitationService();
  const { eventUUID } = useParams();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setWaitingFetch(true);
    setTouched(false);
    setMessageError({});
    try {
      if (eventUUID) {
        const invitation = await userEventInvitation({
          userField: userField,
          eventUuid: eventUUID,
        } as UserInvitation);
        addNotification({
          title: "Convite enviado",
          message: `Convite enviado para ${invitation.ownerUser.firstname}`,
          type: MessageType.OK,
        });
        setUserField("");
        navigate(-1);
      } else {
        addNotification({
          title: "Convite não pode ser enviado",
          message: `Convite não pode ser enviado quando nenhum evento foi selecionado`,
          type: MessageType.WARNING,
        });
      }
    } catch (error: ApiError | any) {
      if (isApiError(error)) setMessageError(mapFieldErrors(error.fields));
    } finally {
      setTouched(true);
      setWaitingFetch(false);
    }
  };

  return (
    <>
      <div className={styles.modal}>
        <h2>Envie um convite</h2>
        <form onSubmit={handleSubmit}>
          <GeneralInput
            value={userField}
            onChange={(e) => setUserField(e.target.value)}
            id="userField"
            placeholder="Nome de usuário ou Nome completo ou Email"
            isRequired
            showStatus={touched}
            message={messageError["userField"]}
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
