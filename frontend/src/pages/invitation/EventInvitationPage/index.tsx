import styles from "./EventInvitationPage.module.scss";
import { CheckCircleSVG, XCircleSVG } from "@/assets/svg";
import Button from "@/components/util/Button";
import PageSelect from "@/components/util/PageSelect";
import {
  InviteMetadata,
  InviteQuery,
} from "@/components/util/InviteParamsSelect/inviteMetadata";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import {
  MessageType,
  useAlertsContext,
} from "@context/AlertContext/useAlertContext";
import { useUserContext } from "@context/UserContext/useUserContext";
import EventInvitation, { AcceptInvitation } from "@data/EventInvitation";
import { initialPageState, pageReducer } from "@reducer/pageReducer";
import useEventInvitationService from "@service/useEventInvitationService";
import { useCallback, useEffect, useReducer, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import InviteParamsSelect from "@/components/util/InviteParamsSelect";

function EventInvitationPage() {
  const [invites, setInvites] = useState<EventInvitation[]>([]);
  const [inviteParam, setInviteParam] = useState<InviteQuery>(InviteQuery.NULL);
  const [page, pageDispatch] = useReducer(pageReducer, initialPageState);
  const { listEventInvitations, acceptedEventInvitation } =
    useEventInvitationService();
  const { addNotification } = useAlertsContext();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();

  const fetchEventInvitations = useCallback(async () => {
    const eventResponse = await listEventInvitations(
      InviteMetadata[inviteParam].boolean,
      page.number,
    );
    if (eventResponse) {
      setInvites(eventResponse.content);
    }
  }, [inviteParam, page.number, setInvites]);

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
    addNotification({
      title: `Convite ${accept ? "Aceito" : "Recusado"}`,
      message: `Convite para evento ${invitation.eventName} ${accept ? "Aceito" : "Recusado"}${accept && ", veja a lista de eventos que participa"}`,
      type: accept ? MessageType.OK : MessageType.INFO,
    });
    fetchEventInvitations();
  };

  return (
    <div className={styles.body}>
      <h2 className={styles.title}>Convites pendentes</h2>
      <div className={styles.header}>
        <InviteParamsSelect
          invite={inviteParam}
          onChange={(e) => setInviteParam(e.target.value as InviteQuery)}
        />
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
      <ul className={styles.list}>
        {invites.map((invite) => (
          <li key={invite.uuid} className={styles.eventCard}>
            <div className={styles.fields}>
              <p className={styles.eventTitle}>{invite.eventName}</p>
              <p className={styles.eventDescription}>{invite.description}</p>
            </div>
            <div className={styles.fields}>
              <p className={styles.eventDate}>{invite.eventDate}</p>
              <p className={styles.ownerUser}>
                Anfitrião: {invite.ownerUser.firstname}
              </p>
            </div>
            {invite.accepted === null && (
              <div className={styles.action}>
                <Button onClick={() => handleInvitation(invite.uuid, false)}>
                  <p>Recusar</p>
                  <XCircleSVG />
                </Button>
                <Button onClick={() => handleInvitation(invite.uuid, true)}>
                  <p>Aceitar</p>
                  <CheckCircleSVG />
                </Button>
              </div>
            )}
          </li>
        ))}
      </ul>
      <PageSelect value={page.number} max={page.max} dispatch={pageDispatch} />
    </div>
  );
}

export default EventInvitationPage;
