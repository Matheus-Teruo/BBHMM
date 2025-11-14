import styles from "./PageSelect.module.scss";
import { ChevronLeftSVG, ChevronRightSVG } from "@/assets/svg";
import Button from "@/components/util/Button";
import { PageAction } from "@/reducer/pageReducer";

interface PageSelectProps {
  value: number;
  max: number;
  dispatch: React.Dispatch<PageAction>;
  className?: string;
}

function PageSelect({ value, max, dispatch, className }: PageSelectProps) {
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const displayedValue = parseInt(event.target.value, 10);
    if (!isNaN(displayedValue)) {
      const backendValue = displayedValue - 1;
      if (backendValue >= 0 && backendValue < max) {
        dispatch({ type: "SET_PAGE_NUMBER", payload: backendValue });
      }
    }
  };
  return (
    <div className={`${styles.body} ${className}`}>
      <Button
        onClick={() =>
          dispatch({ type: "SET_PAGE_NUMBER", payload: value - 1 })
        }
        disabled={value <= 0}
      >
        <ChevronLeftSVG size={16} />
      </Button>
      <input
        name="page"
        type="number"
        value={value + 1}
        onChange={handleInputChange}
        min={1}
        max={max}
      />
      <Button
        onClick={() =>
          dispatch({ type: "SET_PAGE_NUMBER", payload: value + 1 })
        }
        disabled={value + 1 >= max}
      >
        <ChevronRightSVG size={16} />
      </Button>
    </div>
  );
}

export default PageSelect;
