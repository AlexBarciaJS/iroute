"use client";

import { useState } from "react";
import { processCommerce, type QuarantineRecord } from "@/lib/api";

export default function ProcessPage() {
  const [processDate, setProcessDate] = useState("2026-07-22");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [quarantineCount, setQuarantineCount] = useState<number | null>(null);
  const [records, setRecords] = useState<QuarantineRecord[]>([]);

  async function onProcess() {
    setLoading(true);
    setError(null);
    setSuccess(null);
    setQuarantineCount(null);
    setRecords([]);

    try {
      const result = await processCommerce(processDate);
      setSuccess(result.message);
      setQuarantineCount(result.quarantineCount);
      setRecords(result.records ?? []);
    } catch (err) {
      setError(err instanceof Error ? err.message : "No se pudo procesar la fecha");
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="panel">
      <h1>Procesar por fecha</h1>
      <p className="lead">
        Ejecuta las validaciones de <code>pc_nomcomred</code> y{" "}
        <code>pc_numdoc</code> para el día indicado. Los registros inválidos se
        mueven a <code>commerce_quarantine</code> con su motivo.
      </p>

      <div className="field">
        <label htmlFor="process-date">Fecha de proceso (pc_processdate)</label>
        <input
          id="process-date"
          type="date"
          value={processDate}
          onChange={(event) => setProcessDate(event.target.value)}
        />
      </div>

      {error ? <div className="alert alertError">{error}</div> : null}
      {success ? <div className="alert alertOk">{success}</div> : null}
      {quarantineCount !== null ? (
        <div className="alert alertWarn">
          Registros insertados en cuarentena en esta ejecución:{" "}
          <strong>{quarantineCount}</strong>
        </div>
      ) : null}

      <div className="actions">
        <button
          className="btn btnPrimary"
          type="button"
          disabled={!processDate || loading}
          onClick={onProcess}
        >
          {loading ? "Procesando..." : "Procesar día"}
        </button>
      </div>

      {records.length > 0 ? (
        <>
          <p className="empty">Resultados para {processDate}:</p>
          <div className="tableWrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>pc_codcom</th>
                  <th>pc_nomcomred</th>
                  <th>pc_numdoc</th>
                  <th>motivo</th>
                </tr>
              </thead>
              <tbody>
                {records.map((row) => (
                  <tr key={row.id}>
                    <td>{row.id}</td>
                    <td>{row.pcCodcom}</td>
                    <td>{row.pcNomcomred || "—"}</td>
                    <td>{row.pcNumdoc || "—"}</td>
                    <td>
                      <span className="badge">{row.motivo}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      ) : null}
    </section>
  );
}
