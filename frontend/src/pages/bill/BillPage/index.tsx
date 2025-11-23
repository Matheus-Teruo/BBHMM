import Button from "@/components/util/Button";
import { isUserLogged, isUserUnlogged } from "@/util/checkAuthentication";
import { useUserContext } from "@context/UserContext/useUserContext";
import { BillResume, UpdateBillParticipants } from "@data/Bills";
import { UserList } from "@data/User";
import useBillsService from "@service/useBillsService";
import useEventService from "@service/useEventService";
import { useCallback, useEffect, useState } from "react";
import FormBill from "../FormBill";
import { EditSVG } from "@/assets/svg";
import { isSuccess } from "@/util/requestHelper";
import usePaymentService from "@service/usePaymentService";
import { DebitTotal } from "@data/Payment";
import { useNavigate, useParams } from "react-router-dom";

function BillPage() {
  const [bills, setBills] = useState<BillResume[]>([]);
  const [users, setUsers] = useState<UserList[]>([]);
  const [debitTotal, setDebitTotal] = useState<DebitTotal>({
    debit: true,
    value: 0,
  });
  const [billForm, setBillForm] = useState<null | "Create" | "Update">(null);
  const [selectedBill, setSelectedBill] = useState<BillResume | undefined>();
  const { listUserFromEvent } = useEventService();
  const { listBills, updateBillParticipant } = useBillsService();
  const { getDebitTotal } = usePaymentService();
  const { user } = useUserContext();
  const navigate = useNavigate();
  const { eventUUID } = useParams();

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
  }, [eventUUID, listBills]);

  useEffect(() => {
    if (isUserLogged(user)) {
      fetchBill();
      fetchUsers();
      fetchPayment();
    } else if (isUserUnlogged(user)) {
      navigate("/auth/login");
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
      fetchPayment();
    }
  };

  function updateBill(bill: BillResume) {
    setSelectedBill(bill);
    setBillForm("Update");
  }

  return (
    <div>
      <h2>Contas</h2>
      <div>
        <div>
          <Button onClick={() => setBillForm("Create")}>Nova conta</Button>
          <Button onClick={() => navigate(`/event/${eventUUID}/bills`)}>
            Pagamentos
          </Button>
          <Button onClick={() => navigate(`/event/${eventUUID}/bills`)}>
            Convidar
          </Button>
        </div>
        <div>
          <p>Total:</p>
          <p>R${debitTotal.value.toFixed(2)}</p>
        </div>
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
                <Button onClick={() => updateBill(bill)}>
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
        />
      )}
    </div>
  );
}

export default BillPage;
