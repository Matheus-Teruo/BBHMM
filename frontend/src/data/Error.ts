export default interface ApiError {
  timestamp: string;
  error: string;
  title: string;
  message: string;
  path: string;
  fields: FieldErrorDetail[];
}

export interface FieldErrorDetail {
  field: string;
  message: string;
}