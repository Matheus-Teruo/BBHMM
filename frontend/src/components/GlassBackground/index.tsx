import styles from "./GlassBackground.module.scss";

function GlassBackground({
  onClick,
  className,
}: {
  onClick: () => void;
  className?: string;
}) {
  return (
    <div className={`${styles.background} ${className}`} onClick={onClick} />
  );
}

export default GlassBackground;
