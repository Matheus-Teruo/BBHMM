import { CheckCircleSVG, XCircleSVG } from "@/assets/svg";
import Button from "@/components/util/Button";
import PageSelect from "@/components/util/PageSelect";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import {
  isMessage,
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import EventInvitation, { AcceptInvitation } from "@data/EventInvitation";
import { initialPageState, pageReducer } from "@reducer/pageReducer";
import useEventInvitationService from "@service/useEventInvitationService";
import { useCallback, useEffect, useReducer, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function EventInvitationPage() {
  const [invites, setInvites] = useState<EventInvitation[]>([]);
  const [page, pageDispatch] = useReducer(pageReducer, initialPageState);
  const { listEventInvitations, acceptedEventInvitation } =
    useEventInvitationService();
  const { addNotification } = useAlertsContext();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();

  const fetchEventInvitations = useCallback(async () => {
    const eventResponse = await listEventInvitations(page.number);
    if (eventResponse) {
      setInvites(eventResponse.content);
    }
  }, [page.number, setInvites]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchEventInvitations();
    } else if (isUserUnlogged(user)) {
      navigate("/auth/login");
    }
  }, [user, fetchEventInvitations]);

  const handleInvitation = async (uuid: string, accept: boolean) => {
    const invitation = await acceptedEventInvitation({
      uuid: uuid,
      accept: accept,
    } as AcceptInvitation);
    if (invitation && !isMessage(invitation)) {
      addNotification({
        title: `Convite ${accept ? "Aceito" : "Recusado"}`,
        message: `Convite para evento ${invitation.eventName} aceito, veja a lista de eventos que participa`,
        type: MessageType.OK,
      });
    }
  };

  return (
    <div>
      <h2>Convites pendentes</h2>
      <div>
        <Button
          onClick={() =>
            navigate("/invites/new/", {
              state: {
                backgroundLocation: {
                  pathname: location.pathname,
                  search: location.search,
                },
              },
            })
          }
        >
          Novo convite
        </Button>
      </div>
      <ul>
        {invites.map((invite) => (
          <li key={invite.uuid}>
            <p>{invite.eventName}</p>
            <p>{invite.eventDate}</p>
            <p>{invite.ownerUser.firstName}</p>
            <Button onClick={() => handleInvitation(invite.uuid, true)}>
              <p>Aceitar</p>
              <CheckCircleSVG />
            </Button>
            <Button onClick={() => handleInvitation(invite.uuid, false)}>
              <p>Recusar</p>
              <XCircleSVG />
            </Button>
          </li>
        ))}
      </ul>
      <PageSelect value={page.number} max={page.max} dispatch={pageDispatch} />
    </div>
  );
}

export default EventInvitationPage;
