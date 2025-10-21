import User from "@data/User";

export function isUserLogged(user: User | null | "unlogged"): user is User {
  return user !== null && user !== "unlogged";
}

export function isUserUnlogged(
  user: User | null | "unlogged",
): user is "unlogged" {
  return user !== null && user === "unlogged";
}