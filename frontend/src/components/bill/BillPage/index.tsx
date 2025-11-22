import Button from "@/components/util/Button";
import { isUserLogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import { BillResume, UpdateBillParticipants } from "@data/Bills";
import Event from "@data/Event";
import { UserResume } from "@data/User";
import useBillsService from "@service/useBillsService";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useState } from "react";
import FormBill from "../FormBill";
import { EditSVG } from "@/assets/svg";
import { isSuccess } from "@/util/requestHelper";
import InviteUserForm from "@/components/event/InviteUserForm";

interface BillPageProps {
  event: Event;
}

function BillPage({ event }: BillPageProps) {
  const [bills, setBills] = useState<BillResume[]>([]);
  const [users, setUsers] = useState<UserResume[]>([]);
  const [billForm, setBillForm] = useState<null | "Create" | "Update">(null);
  const [inviteUserForm, setInviteUserForm] = useState<boolean>(false);
  const [selectedBill, setSelectedBill] = useState<BillResume | undefined>();
  const { listUserFromEvent } = useEventService();
  const { listBills, updateBillParticipant } = useBillsService();
  const { user } = useUserContext();

  const fetchUsers = useCallback(async () => {
    const usersResponse = await listUserFromEvent(event.uuid);
    if (usersResponse) {
      setUsers(usersResponse);
    }
  }, [event, listUserFromEvent]);

  const fetchBill = useCallback(async () => {
    const billResponse = await listBills(event.uuid);
    if (billResponse) {
      setBills(billResponse);
    }
  }, [event, listBills]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchBill();
      fetchUsers();
    }
  }, [user, fetchBill]);

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
    }
  };

  return (
    <div>
      <h2>Contas</h2>
      <div>
        <Button onClick={() => setBillForm("Create")}>Nova conta</Button>
        <Button onClick={() => setInviteUserForm(true)}>Convidar</Button>
      </div>
      <div>
        <ul>
          <li>
            <p>Nome</p>
            <p>Valor</p>
            {users.map((user) => (
              <p key={user.uuid}>{user.firstName}</p>
            ))}
            <div />
          </li>
          {bills.map((bill) => (
            <li key={bill.uuid}>
              <p>{bill.billName}</p>
              <p>{bill.value}</p>
              {users.map((user) => (
                <input
                  type="checkbox"
                  checked={bill.participantsUuid.includes(user.uuid)}
                  onChange={(e) =>
                    handleCheckBill(e.target.checked, user.uuid, bill.uuid)
                  }
                />
              ))}
              <div>
                <Button onClick={() => setSelectedBill(bill)}>
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
          eventUuid={event.uuid}
          onChange={() => {
            fetchBill();
            return setBillForm(null);
          }}
        />
      )}
      {inviteUserForm && (
        <InviteUserForm
          event={event}
          onChange={() => {
            fetchUsers();
            return setInviteUserForm(false);
          }}
        />
      )}
    </div>
  );
}

export default BillPage;
