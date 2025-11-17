import { isUserLogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import { BillResume } from "@data/Bills";
import Event from "@data/Event";
import { UserResume } from "@data/User";
import useBillsService from "@service/useBillsService";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useState } from "react";

interface BillPageProps {
  event: Event;
}

function BillPage({ event }: BillPageProps) {
  const [bills, setBills] = useState<BillResume[]>([]);
  const [users, setUsers] = useState<UserResume[]>([]);
  const { listUserFromEvent } = useEventService();
  const { listBills } = useBillsService();
  const { user } = useUserContext();

  const fetchBill = useCallback(async () => {
    const billResponse = await listBills(event.uuid);
    const usersResponse = await listUserFromEvent(event.uuid);
    if (billResponse) {
      setBills(billResponse);
    }
    if (usersResponse) {
      setUsers(usersResponse);
    }
  }, [event, listBills]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchBill();
    }
  }, [user, fetchBill]);

  return (
    <div>
      <h2>Contas</h2>
      <div>
        <ul>
          <li>
            <p>Nome</p>
            <p>Valor</p>
            {users.map((user) => (
              <p key={user.uuid}>{user.firstName}</p>
            ))}
          </li>
          {bills.map((bill) => (
            <li key={bill.uuid}>
              <p>{bill.billName}</p>
              <p>{bill.value}</p>
              {users.map((user) => (
                <input
                  type="checkbox"
                  checked={bill.participantsUuid.includes(user.uuid)}
                />
              ))}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default BillPage;
