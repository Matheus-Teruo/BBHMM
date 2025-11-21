import { AxiosResponse } from "axios";

export const isSuccess = (res?: AxiosResponse | null) =>
  !!res && res.status >= 200 && res.status < 300;
