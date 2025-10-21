import { SVGProps } from "../SVGprops";

const CircleSVG = ({
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
    <circle cx="12" cy="12" r="10"></circle>
  </svg>
);

export default CircleSVG;
