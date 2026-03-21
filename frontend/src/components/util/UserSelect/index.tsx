import styles from "./UserSelect.module.scss";
import { InputStatus } from "../InputStatus";
import { useEffect, useState } from "react";
import { EventUser } from "@data/User";
import useEventService from "@service/useEventService";
import { useUserContext } from "@context/UserContext/useUserContext";

interface UserSelectProps {
  value: string | undefined;
  onChange: (event: React.ChangeEvent<HTMLSelectElement>) => void;
  showStatus?: boolean;
  message?: string;
}

function UserSelect({
  value,
  onChange,
  showStatus = false,
  message = "",
}: UserSelectProps) {
  const [listUser, setListUsers] = useState<EventUser[]>([]);
  const [status, setStatus] = useState<InputStatus>(InputStatus.Untouched);
  const { listUserFromEvent } = useEventService();
  const { event } = useUserContext();

  useEffect(() => {
    const fetchUser = async () => {
      if (event) {
        const users = await listUserFromEvent(event.uuid);
        if (users) {
          setListUsers(users);
        }
      }
    };
    fetchUser();
  }, [setListUsers]);

  useEffect(() => {
    if (showStatus) {
      if (message === "") {
        setStatus(InputStatus.Accepted);
      } else {
        setStatus(InputStatus.Rejected);
      }
    }
  }, [showStatus, message]);

  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    onChange(event);
    setStatus(InputStatus.Untouched);
  };

  return (
    <div
      className={`${styles.base}
        ${
          status === InputStatus.Accepted
            ? styles.unfocOK
            : status === InputStatus.Rejected && styles.unfocNO
        }`}
    >
      <select
        className={styles.select}
        id="users"
        value={value}
        onChange={handleChange}
      >
        <option value="" disabled style={{ color: "#656360" }}>
          -- usuário --
        </option>
        {listUser.map((user) => (
          <option key={user.uuid} value={user.uuid}>
            {user.fullname}
          </option>
        ))}
      </select>
      {status !== InputStatus.Untouched && message && (
        <span className={styles.messageError}>{message}</span>
      )}
    </div>
  );
}

export default UserSelect;
