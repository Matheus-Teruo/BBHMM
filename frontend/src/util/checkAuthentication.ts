import { UserResume } from "@data/User";

export function isUserLogged(
  user: UserResume | null | "unlogged",
): user is UserResume {
  return user !== null && user !== "unlogged";
}

export function isUserUnlogged(
  user: UserResume | null | "unlogged",
): user is "unlogged" {
  return user !== null && user === "unlogged";
}
