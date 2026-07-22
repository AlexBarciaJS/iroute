"use client";

import { useMemo, useState } from "react";
import { uploadCommerceCsv } from "@/lib/api";
import { isValidCommerceFileName, parseCommerceCsv, type CommercePreviewRow } from "@/lib/csv";

export default function UploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [rows, setRows] = useState<CommercePreviewRow[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const canSubmit = useMemo(
    () => Boolean(file && rows.length > 0 && !loading),
    [file, rows.length, loading],
  );

  async function onFileChange(event: React.ChangeEvent<HTMLInputElement>) {
    setError(null);
    setSuccess(null);

    const selected = event.target.files?.[0] ?? null;
    setFile(selected);
    setRows([]);

    if (!selected) {
      return;
    }

    if (!isValidCommerceFileName(selected.name)) {
      setError("El archivo debe denominarse commerce_DDMMYYYY.csv");
      return;
    }

    if (selected.size === 0) {
      setError("El archivo CSV no debe estar vacío");
      return;
    }

    const content = await selected.text();
    const parsed = parseCommerceCsv(content);
    if (parsed.length === 0) {
      setError("No hay filas para previsualizar. Verifica el contenido del CSV.");
      return;
    }

    setRows(parsed);
  }

  async function onSubmit() {
    if (!file) {
      return;
    }

    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const result = await uploadCommerceCsv(file);
      setSuccess(result.message);
    } catch (err) {
      setError(err instanceof Error ? err.message : "No se pudo cargar el archivo");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="panel">
      <h1>Carga de comercios</h1>
      <p className="lead">
        Selecciona el archivo <strong>commerce_DDMMYYYY.csv</strong>, previsualiza
        las filas y envíalo al backend para registrarlo con{" "}
        <code>sp_create_commerce</code>.
      </p>

      <div className="field">
        <label htmlFor="csv-file">Archivo CSV</label>
        <input
          id="csv-file"
          type="file"
          accept=".csv,text/csv"
          onChange={onFileChange}
        />
      </div>

      {error ? <div className="alert alertError">{error}</div> : null}
      {success ? <div className="alert alertOk">{success}</div> : null}

      <div className="actions">
        <button className="btn btnPrimary" type="button" disabled={!canSubmit} onClick={onSubmit}>
          {loading ? "Enviando..." : "Enviar al backend"}
        </button>
      </div>

      {rows.length === 0 ? (
        <p className="empty">Aún no hay datos para previsualizar.</p>
      ) : (
        <>
          <p className="empty">{rows.length} registros listos para enviar.</p>
          <div className="tableWrap">
            <table>
              <thead>
                <tr>
                  <th>pc_codcom</th>
                  <th>pc_nomcomred</th>
                  <th>pc_tipdoc</th>
                  <th>pc_numdoc</th>
                  <th>pc_processdate</th>
                </tr>
              </thead>
              <tbody>
                {rows.map((row, index) => (
                  <tr key={`${row.pc_codcom}-${index}`}>
                    <td>{row.pc_codcom}</td>
                    <td>{row.pc_nomcomred || <span className="badge">vacío</span>}</td>
                    <td>{row.pc_tipdoc}</td>
                    <td>{row.pc_numdoc || <span className="badge">vacío</span>}</td>
                    <td>{row.pc_processdate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </section>
  );
}
