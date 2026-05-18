export function takeInitial(fullName?: string) {
  if (!fullName || typeof fullName !== 'string') {
    return '?';
  }

  const parts = fullName
    .trim()
    .split(/\s+/)
    .filter(Boolean);

  if (parts.length === 0) {
    return '?';
  }

  const first = parts[0]?.[0] || '';
  const last = parts[parts.length - 1]?.[0] || '';

  return `${first}${last}`.toUpperCase();
}