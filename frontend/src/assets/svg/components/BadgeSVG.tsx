import { SVGProps } from "../SVGprops";

const BadgeSVG = ({
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
    <rect x="2" y="5" width="20" height="14" rx="2"></rect>
    <rect x="10" y="2" width="4" height="5" rx="1"></rect>
    <circle cx="8" cy="11" r="2"></circle>
    <path d="M6 16c1-2 4-2 4 0"></path>
    <line x1="13" y1="11" x2="19" y2="11"></line>
    <line x1="13" y1="14" x2="19" y2="14"></line>
  </svg>
);

export default BadgeSVG;
