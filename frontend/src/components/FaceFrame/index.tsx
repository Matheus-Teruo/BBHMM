import { takeInitial } from "@/util/initialHelper";
import styles from "./FaceFrame.module.scss";
import { useState } from "react";

function FaceFrame({
  imageUrl,
  fullname,
  onClick,
  userColor = "#FFFFFF",
  small = false,
}: {
  imageUrl?: string;
  fullname: string;
  onClick?: () => void;
  userColor?: string
  small?: boolean
}) {
  const [imageError, setImageError] = useState(false)

  const showImage = imageUrl && !imageError

  return (
    <div
      className={`${styles.frame} ${small ? styles.small : styles.large}`}
      onClick={onClick}
    >
      {showImage ? (
        <img
          src={imageUrl}
          className={styles.imageFrame}
          onError={() => setImageError(true)}
        />
      ) : (
        <div
          className={styles.emptyFrame}
          style={{ backgroundColor: userColor }}
        >
          <p>{takeInitial(fullname)}</p>
        </div>
      )}
    </div>
  )
}

export default FaceFrame