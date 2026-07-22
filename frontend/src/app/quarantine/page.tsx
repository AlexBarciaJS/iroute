"use client";

import { useCallback, useEffect, useState } from "react";
import { listQuarantine, type QuarantineRecord } from "@/lib/api";

export default function QuarantinePage() {
  const [records, setRecords] = useState<QuarantineRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listQuarantine();
      setRecords(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "No se pudo listar la cuarentena");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void load();
  }, [load]);

  return (
    <section className="panel">
      <h1>Registros en cuarentena</h1>
      <p className="lead">
        Visualiza los comercios rechazados y el motivo de su envío a{" "}
        <code>commerce_quarantine</code>.
      </p>

      <div className="actions">
        <button className="btn btnSecondary" type="button" onClick={() => void load()} disabled={loading}>
          {loading ? "Actualizando..." : "Actualizar"}
        </button>
      </div>

      {error ? <div className="alert alertError">{error}</div> : null}

      {!loading && records.length === 0 ? (
        <p className="empty">No hay registros en cuarentena.</p>
      ) : null}

      {records.length > 0 ? (
        <div className="tableWrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>pc_codcom</th>
                <th>pc_nomcomred</th>
                <th>pc_numdoc</th>
                <th>pc_processdate</th>
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
                  <td>{row.pcProcessdate}</td>
                  <td>
                    <span className="badge">{row.motivo}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : null}
    </section>
  );
}
