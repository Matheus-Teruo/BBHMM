import { InviteMetadata, InviteQuery } from "./inviteMetadata";
import styles from "./InviteParamsSelect.module.scss";

type SelectPaymentProps = {
  invite: InviteQuery;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
};

function InviteParamsSelect({ invite, onChange }: SelectPaymentProps) {
  return (
    <ul className={styles.radio}>
      {Object.entries(InviteMetadata).map(([key, { pt }]) => (
        <label key={key} className={`${key === invite && styles.selected}`}>
          <input
            type="radio"
            name="inviteOptions"
            value={key}
            checked={invite === key}
            onChange={onChange}
          />
          <span>{pt}</span>
        </label>
      ))}
    </ul>
  );
}

export default InviteParamsSelect;
