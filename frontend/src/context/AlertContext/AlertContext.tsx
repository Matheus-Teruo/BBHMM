import React, { useCallback, useState } from "react";
import { Message, Notification, AlertsContext } from "./useAlertContext";

export default function AlertProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  const addNotification = useCallback((message: Message): void => {
    setNotifications((prev) => {
      const newNotification: Notification = {
        id: Date.now(),
        message: message,
      };
      return [newNotification, ...prev];
    });
  }, []);

  const removeNotification = useCallback((id: number): void => {
    setNotifications((prevNotifications) =>
      prevNotifications.filter((notification) => notification.id !== id),
    );
  }, []);

  return (
    <AlertsContext.Provider
      value={{ notifications, addNotification, removeNotification }}
    >
      {children}
    </AlertsContext.Provider>
  );
}
