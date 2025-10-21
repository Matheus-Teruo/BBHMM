import { SVGProps } from "../SVGprops";

const ChevronLeftSVG = ({
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
    <polyline points="15 18 9 12 15 6"></polyline>
  </svg>
);

export default ChevronLeftSVG;
