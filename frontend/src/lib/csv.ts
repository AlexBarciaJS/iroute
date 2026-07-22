export type CommercePreviewRow = {
  pc_codcom: string;
  pc_nomcomred: string;
  pc_tipdoc: string;
  pc_numdoc: string;
  pc_processdate: string;
};

export function parseCommerceCsv(content: string): CommercePreviewRow[] {
  const lines = content
    .replace(/^\uFEFF/, "")
    .split(/\r?\n/)
    .map((line) => line.trim())
    .filter((line) => line.length > 0);

  if (lines.length <= 1) {
    return [];
  }

  const rows: CommercePreviewRow[] = [];
  for (let i = 1; i < lines.length; i += 1) {
    const parts = lines[i].split(",", -1);
    rows.push({
      pc_codcom: (parts[0] ?? "").trim(),
      pc_nomcomred: (parts[1] ?? "").trim(),
      pc_tipdoc: (parts[2] ?? "").trim(),
      pc_numdoc: (parts[3] ?? "").trim(),
      pc_processdate: (parts[4] ?? "").trim(),
    });
  }

  return rows;
}

export function isValidCommerceFileName(name: string): boolean {
  return /^commerce_\d{8}\.csv$/i.test(name);
}
