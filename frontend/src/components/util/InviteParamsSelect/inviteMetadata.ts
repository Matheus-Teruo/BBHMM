export enum InviteQuery {
  ACCEPTED = "accepted",
  NULL = "null",
  REFUSED = "refused",
}

export const InviteMetadata: Record<
  InviteQuery,
  { pt: string; boolean: boolean | undefined }
> = {
  [InviteQuery.ACCEPTED]: { pt: "Aceito", boolean: true },
  [InviteQuery.NULL]: { pt: "Pendente", boolean: undefined },
  [InviteQuery.REFUSED]: { pt: "Recusado", boolean: false },
};
