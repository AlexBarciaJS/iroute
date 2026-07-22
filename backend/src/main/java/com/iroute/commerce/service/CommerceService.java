package com.iroute.commerce.service;

import com.iroute.commerce.dto.CommerceRowDto;
import com.iroute.commerce.dto.ProcessResponse;
import com.iroute.commerce.dto.QuarantineResponse;
import com.iroute.commerce.dto.UploadResponse;
import com.iroute.commerce.entity.CommerceQuarantine;
import com.iroute.commerce.exception.BusinessException;
import com.iroute.commerce.repository.CommerceQuarantineRepository;
import com.iroute.commerce.repository.CommerceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CommerceService {

    private final CsvCommerceParser csvCommerceParser;
    private final EntityManager entityManager;
    private final CommerceQuarantineRepository quarantineRepository;
    private final CommerceRepository commerceRepository;

    public CommerceService(
            CsvCommerceParser csvCommerceParser,
            EntityManager entityManager,
            CommerceQuarantineRepository quarantineRepository,
            CommerceRepository commerceRepository) {
        this.csvCommerceParser = csvCommerceParser;
        this.entityManager = entityManager;
        this.quarantineRepository = quarantineRepository;
        this.commerceRepository = commerceRepository;
    }

    @Transactional
    public UploadResponse uploadCommerceCsv(MultipartFile file) {
        List<CommerceRowDto> rows = csvCommerceParser.parse(file);

        for (CommerceRowDto row : rows) {
            StoredProcedureQuery query = entityManager
                    .createStoredProcedureQuery("sp_create_commerce");
            query.registerStoredProcedureParameter("p_pc_codcom", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_pc_nomcomred", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_pc_tipdoc", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_pc_numdoc", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_pc_processdate", LocalDate.class, ParameterMode.IN);

            query.setParameter("p_pc_codcom", row.getPcCodcom());
            query.setParameter("p_pc_nomcomred", emptyToNull(row.getPcNomcomred()));
            query.setParameter("p_pc_tipdoc", emptyToNull(row.getPcTipdoc()));
            query.setParameter("p_pc_numdoc", emptyToNull(row.getPcNumdoc()));
            query.setParameter("p_pc_processdate", row.getPcProcessdate());
            query.execute();
        }

        return new UploadResponse(
                rows.size(),
                "Se registraron " + rows.size() + " comercios correctamente");
    }

    @Transactional
    public ProcessResponse processByDate(LocalDate processDate) {
        if (processDate == null) {
            throw new BusinessException("La fecha de proceso es obligatoria", HttpStatus.BAD_REQUEST);
        }

        long pendingForDate = commerceRepository.countByPcProcessdate(processDate);
        if (pendingForDate == 0) {
            List<QuarantineResponse> existing = quarantineRepository
                    .findByPcProcessdateOrderByIdDesc(processDate)
                    .stream()
                    .map(this::toQuarantineResponse)
                    .toList();

            return new ProcessResponse(
                    processDate,
                    0,
                    "No hay registros pendientes en commerce para la fecha "
                            + processDate
                            + ". Si ya procesaste el CSV, revisa Cuarentena.",
                    existing);
        }

        Long maxIdBefore = quarantineRepository.findMaxId();
        long previousMaxId = maxIdBefore == null ? 0L : maxIdBefore;

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("sp_process_commerce");
        query.registerStoredProcedureParameter("p_process_date", LocalDate.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_quarantine_count", Integer.class, ParameterMode.OUT);
        query.setParameter("p_process_date", processDate);
        query.execute();

        // Forzar flush/clear para leer lo insertado por el SP en esta misma transacción
        entityManager.flush();
        entityManager.clear();

        Integer quarantineCount = (Integer) query.getOutputParameterValue("p_quarantine_count");
        List<QuarantineResponse> movedRecords = quarantineRepository
                .findByIdGreaterThanAndPcProcessdateOrderByIdAsc(previousMaxId, processDate)
                .stream()
                .map(this::toQuarantineResponse)
                .toList();

        int count = quarantineCount != null ? quarantineCount : movedRecords.size();
        if (count == 0) {
            count = movedRecords.size();
        }

        if (count == 0) {
            List<QuarantineResponse> existing = quarantineRepository
                    .findByPcProcessdateOrderByIdDesc(processDate)
                    .stream()
                    .map(this::toQuarantineResponse)
                    .toList();

            return new ProcessResponse(
                    processDate,
                    0,
                    existing.isEmpty()
                            ? "No se encontraron registros inválidos para la fecha " + processDate
                            : "No hay nuevos inválidos. Ya existen "
                                    + existing.size()
                                    + " registros en cuarentena para esa fecha.",
                    existing);
        }

        return new ProcessResponse(
                processDate,
                count,
                "Se movieron " + count + " registros a commerce_quarantine",
                movedRecords);
    }

    @Transactional(readOnly = true)
    public List<QuarantineResponse> listQuarantine() {
        return quarantineRepository.findAllByOrderByIdDesc().stream()
                .map(this::toQuarantineResponse)
                .toList();
    }

    private QuarantineResponse toQuarantineResponse(CommerceQuarantine entity) {
        QuarantineResponse response = new QuarantineResponse();
        response.setId(entity.getId());
        response.setPcCodcom(entity.getPcCodcom());
        response.setPcNomcomred(entity.getPcNomcomred());
        response.setPcTipdoc(entity.getPcTipdoc());
        response.setPcNumdoc(entity.getPcNumdoc());
        response.setPcProcessdate(entity.getPcProcessdate());
        response.setMotivo(entity.getMotivo());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value;
    }
}
