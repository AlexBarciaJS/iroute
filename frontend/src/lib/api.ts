const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export type UploadResponse = {
  insertedRecords: number;
  message: string;
};

export type ProcessResponse = {
  processDate: string;
  quarantineCount: number;
  message: string;
  records: QuarantineRecord[];
};

export type QuarantineRecord = {
  id: number;
  pcCodcom: string;
  pcNomcomred: string | null;
  pcTipdoc: string | null;
  pcNumdoc: string | null;
  pcProcessdate: string;
  motivo: string;
  createdAt: string | null;
};

export type ApiError = {
  timestamp?: string;
  status: number;
  error: string;
  message: string;
};

async function parseError(response: Response): Promise<string> {
  try {
    const data = (await response.json()) as ApiError;
    return data.message || data.error || "Error inesperado";
  } catch {
    return `Error HTTP ${response.status}`;
  }
}

export async function uploadCommerceCsv(file: File): Promise<UploadResponse> {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(`${API_URL}/api/commerce/upload`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  return response.json();
}

export async function processCommerce(processDate: string): Promise<ProcessResponse> {
  const response = await fetch(`${API_URL}/api/commerce/process`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ processDate }),
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  return response.json();
}

export async function listQuarantine(): Promise<QuarantineRecord[]> {
  const response = await fetch(`${API_URL}/api/commerce/quarantine`, {
    method: "GET",
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error(await parseError(response));
  }

  return response.json();
}
