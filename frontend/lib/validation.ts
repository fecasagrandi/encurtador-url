export function isValidUrl(url: string): boolean {
  try {
    const urlObj = new URL(url);
    return urlObj.protocol === 'http:' || urlObj.protocol === 'https:';
  } catch {
    return false;
  }
}

export function formatUrl(url: string): string {
  if (!url.startsWith('http://') && !url.startsWith('https://')) {
    return `https://${url}`;
  }
  return url;
}

export function validateUrlInput(url: string): { isValid: boolean; error?: string } {
  if (!url.trim()) {
    return { isValid: false, error: 'Por favor, insira uma URL' };
  }

  const formattedUrl = formatUrl(url.trim());
  
  if (!isValidUrl(formattedUrl)) {
    return { isValid: false, error: 'Por favor, insira uma URL válida' };
  }

  if (formattedUrl.length > 2048) {
    return { isValid: false, error: 'URL muito longa (máximo 2048 caracteres)' };
  }

  return { isValid: true };
}
