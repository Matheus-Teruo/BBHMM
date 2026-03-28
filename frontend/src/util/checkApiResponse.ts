import ApiError, { FieldErrorDetail } from "@data/Error";

export function isApiError(
  value: unknown
): value is ApiError {
  return (
    typeof value === "object" &&
    value !== null &&
    "error" in value
  );
}

export function mapFieldErrors(fields: FieldErrorDetail[]): Record<string, string> {
  const record: Record<string, string> = {};

  for (const field of fields) {
    record[field.field] = field.message;
  }

  return record;
}