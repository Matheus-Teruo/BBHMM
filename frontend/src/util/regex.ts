export const regexUuid =
  /^([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}|)$/i;
export const regexLetterNumberSpace = /^[\p{L}\p{N} ]*$/u;
export const regexLetterNumber = /^[\p{L}\p{N}]*$/u;
export const regexLetterSpace = /^[\p{L} ]*$/u;
export const regexPassword = /[\w@#$%^&+=!]*$/u;
export const regexEmail = /^[\p{L}\p{N} @.-]*$/u;
export const regexText = /^[\p{L}\p{N} /:;,.!()?%\\-]*$/u;
export const regexDate =
  /^(?:\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\d|3[01])|)$/;
