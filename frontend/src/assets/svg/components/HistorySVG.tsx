import { SVGProps } from "../SVGprops";

const HistorySVG = ({
  size = 24,
  color = "currentColor",
  className,
}: SVGProps) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={size}
    height={size}
    viewBox="0 0 24 24"
    fill="none"
    stroke={color}
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    className={className}
  >
    <path d="M21.4326 15.0178C24.4195 6.51654 14.463 -1.98466 6 4.01619" />
    <path d="M7 1L6 4L9 5" />
    <path d="M18 19.9337C9.52276 26.1418 -1.44741 17.3469 3.04057 7" />
    <path d="M12 6V12L16 15" />
  </svg>
);

export default HistorySVG;
