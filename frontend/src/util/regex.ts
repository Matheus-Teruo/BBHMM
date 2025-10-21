export const regexUuid =
  /^([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}|)$/i;
export const regexLeterNumberSpace = /^[\p{L}\p{N} ]*$/u;
export const regexLeterNumber = /^[\p{L}\p{N}]*$/u;
export const regexLeterSpace = /^[\p{L} ]*$/u;
export const regexPassword = /[\w@#$%^&+=!]*$/u;
export const regexText = /^[\p{L}\p{N} /:;,.!()?\\-]*$/u;
