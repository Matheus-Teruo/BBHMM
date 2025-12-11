import { SearchSVG } from "@/assets/svg";
import styles from "./SearchFilter.module.scss";

interface OptionsFilterProps {
  value: string;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function SearchFilter({ value, onChange }: OptionsFilterProps) {
  return (
    <div className={styles.background}>
      <label htmlFor="search">
        <input id="search" type="text" value={value} onChange={onChange} />
        <SearchSVG />
      </label>
    </div>
  );
}

export default SearchFilter;
