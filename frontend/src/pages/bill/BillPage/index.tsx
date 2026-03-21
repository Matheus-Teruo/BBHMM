import styles from "./BillPage.module.scss";
import Button from "@/components/util/Button";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import { BillResume, UpdateBillParticipants } from "@data/Bills";
import { Role, EventUser } from "@data/User";
import useBillsService from "@service/useBillsService";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useState } from "react";
import FormBill from "../FormBill";
import { EditSVG } from "@/assets/svg";
import { isSuccess } from "@/util/requestHelper";
import usePaymentService from "@service/usePaymentService";
import { DebitTotal } from "@data/Payment";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import useUserService from "@service/useUserService";
import { isMessage } from "@context/AlertContext/useAlertContext";
import Event from "@data/Event";

function BillPage() {
  const [event, setEvent] = useState<Event>();
  const [bills, setBills] = useState<BillResume[]>([]);
  const [users, setUsers] = useState<EventUser[]>([]);
  const [debitTotal, setDebitTotal] = useState<DebitTotal>({
    debit: true,
    value: 0,
  });
  const [billForm, setBillForm] = useState<null | "Create" | "Update">(null);
  const [selectedBill, setSelectedBill] = useState<BillResume | undefined>();
  const { getEvent, listUserFromEvent } = useEventService();
  const { listBills, updateBillParticipant } = useBillsService();
  const { getDebitTotal } = usePaymentService();
  const { getGuest } = useUserService();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const location = useLocation();
  const { eventUUID } = useParams();

  const fetchEvent = useCallback(async () => {
    if (eventUUID) {
      const eventResponse = await getEvent(eventUUID);
      if (eventResponse) {
        setEvent(eventResponse);
      }
    }
  }, [eventUUID, getEvent]);

  const fetchUsers = useCallback(async () => {
    if (eventUUID) {
      const usersResponse = await listUserFromEvent(eventUUID);
      if (usersResponse) {
        setUsers(usersResponse);
      }
    }
  }, [eventUUID, listUserFromEvent]);

  const fetchBill = useCallback(async () => {
    if (eventUUID) {
      const billResponse = await listBills(eventUUID);
      if (billResponse) {
        setBills(billResponse);
      }
    }
  }, [eventUUID, listBills]);

  const fetchPayment = useCallback(async () => {
    if (eventUUID) {
      const paymentResponse = await getDebitTotal(eventUUID);
      if (paymentResponse) {
        setDebitTotal(paymentResponse);
      }
    }
  }, [eventUUID, getDebitTotal]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchEvent();
      fetchBill();
      fetchUsers();
      fetchPayment();
    } else if (isUserUnlogged(user)) {
      navigate("/auth/login");
    }
  }, [user, fetchBill]);

  useEffect(() => {
    if (location.state?.onCreated) {
      fetchUsers();
      navigate(location.pathname, { replace: true });
    }
  }, [location.state]);

  const handleCheckBill = async (
    value: boolean,
    participant: string,
    billUuid: string,
  ) => {
    const body = {
      type: value ? "add" : "remove",
      uuid: billUuid,
      partUuid: participant,
    } as UpdateBillParticipants;
    const response = await updateBillParticipant(body);
    if (isSuccess(response)) {
      fetchBill();
      fetchPayment();
    }
  };

  function handleInvite() {
    navigate(`/invites/new/${eventUUID}`, {
      state: {
        backgroundLocation: {
          pathname: location.pathname,
          search: location.search,
        },
      },
    });
  }

  function addGuest() {
    navigate("/auth/guest/new/", {
      state: {
        backgroundLocation: {
          pathname: location.pathname,
          search: location.search,
        },
      },
    });
  }

  async function handleUser(user: EventUser) {
    if (user.role == Role.GUEST) {
      if (eventUUID) {
        const guestRespone = await getGuest(user.uuid, eventUUID);
        if (guestRespone && !isMessage(guestRespone)) {
          navigate("/auth/guest/info", {
            state: {
              username: guestRespone.username,
              token: guestRespone.token,
              backgroundLocation: {
                pathname: location.pathname,
                search: location.search,
              },
            },
          });
        }
      }
    } else if (user.role == Role.USER) {
      navigate("/auth/user/info", {
        state: {
          user: user,
          backgroundLocation: {
            pathname: location.pathname,
            search: location.search,
          },
        },
      });
    }
  }

  function updateBill(bill: BillResume) {
    setSelectedBill(bill);
    setBillForm("Update");
  }

  return (
    <div className={styles.body}>
      <h2 className={styles.title}>{event?.eventName}</h2>
      <p>{event?.description}</p>
      <div className={styles.top}>
        <div className={styles.actions}>
          <Button onClick={() => setBillForm("Create")}>
            <p>Nova conta</p>
          </Button>
          <Button onClick={() => handleInvite()}>
            <p>Convidar</p>
          </Button>
          <Button onClick={() => addGuest()}>
            <p>Criar Convidado</p>
          </Button>
        </div>
        <div className={styles.total}>
          <p>Total:</p>
          <p>R${debitTotal.value.toFixed(2)}</p>
        </div>
      </div>
      <div className={styles.tableWrapper}>
        <ul className={styles.list}>
          <li className={`${styles.row} ${styles.header}`}>
            <p className={`${styles.cell} ${styles.name}`}>Conta</p>
            <p className={`${styles.cell} ${styles.value}`}>Valor</p>
            {users.map((user) => (
              <p
                key={user.uuid}
                className={`${styles.cell} ${styles.userHeader}`}
                onClick={() => handleUser(user)}
              >
                {user.firstname}
              </p>
            ))}
            <div className={`${styles.cell} ${styles.actions}`}>
              <p className={`${styles.cell} ${styles.actions}`}>Editar</p>
            </div>
          </li>
          {bills.map((bill) => (
            <li key={bill.uuid} className={styles.row}>
              <p className={`${styles.cell} ${styles.name}`}>{bill.billName}</p>
              <p className={`${styles.cell} ${styles.value}`}>
                R${bill.value.toFixed(2)}
              </p>
              {users.map((user) => (
                <div
                  key={user.uuid}
                  className={`${styles.cell} ${styles.user}`}
                >
                  <input
                    type="checkbox"
                    checked={bill.participantsUuid.includes(user.uuid)}
                    onChange={(e) =>
                      handleCheckBill(e.target.checked, user.uuid, bill.uuid)
                    }
                  />
                </div>
              ))}
              <div className={`${styles.cell} ${styles.actions}`}>
                <Button
                  className={`${styles.cell} ${styles.actions}`}
                  onClick={() => updateBill(bill)}
                >
                  <p>Editar</p>
                  <EditSVG />
                </Button>
              </div>
            </li>
          ))}
        </ul>
      </div>
      {billForm && (
        <FormBill
          form={billForm}
          initialValue={selectedBill}
          eventUuid={eventUUID}
          onChange={() => {
            fetchBill();
            fetchPayment();
            return setBillForm(null);
          }}
          userList={users}
        />
      )}
    </div>
  );
}

export default BillPage;
