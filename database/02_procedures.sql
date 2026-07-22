USE iroutedb;

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_create_commerce$$
CREATE PROCEDURE sp_create_commerce(
    IN p_pc_codcom      VARCHAR(20),
    IN p_pc_nomcomred   VARCHAR(100),
    IN p_pc_tipdoc      VARCHAR(5),
    IN p_pc_numdoc      VARCHAR(20),
    IN p_pc_processdate DATE
)
BEGIN
    INSERT INTO commerce (
        pc_codcom,
        pc_nomcomred,
        pc_tipdoc,
        pc_numdoc,
        pc_processdate
    ) VALUES (
        p_pc_codcom,
        p_pc_nomcomred,
        p_pc_tipdoc,
        p_pc_numdoc,
        p_pc_processdate
    );
END$$

DROP PROCEDURE IF EXISTS sp_process_commerce$$
CREATE PROCEDURE sp_process_commerce(
    IN  p_process_date DATE,
    OUT p_quarantine_count INT
)
BEGIN
    DECLARE v_count INT DEFAULT 0;

    -- Nombre vacío o solo espacios
    INSERT INTO commerce_quarantine (
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate, motivo
    )
    SELECT
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate,
        'El nombre del comercio (nomcomred) se encuentra vacio'
    FROM commerce
    WHERE pc_processdate = p_process_date
      AND (pc_nomcomred IS NULL OR TRIM(pc_nomcomred) = '');

    SET v_count = v_count + ROW_COUNT();

    -- Documento vacío
    INSERT INTO commerce_quarantine (
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate, motivo
    )
    SELECT
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate,
        'El numero (numdoc) se encuentra vacio'
    FROM commerce
    WHERE pc_processdate = p_process_date
      AND (pc_numdoc IS NULL OR TRIM(pc_numdoc) = '')
      AND NOT (pc_nomcomred IS NULL OR TRIM(pc_nomcomred) = '');

    SET v_count = v_count + ROW_COUNT();

    -- Documento con letras o caracteres especiales (solo dígitos permitidos)
    INSERT INTO commerce_quarantine (
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate, motivo
    )
    SELECT
        pc_codcom, pc_nomcomred, pc_tipdoc, pc_numdoc, pc_processdate,
        CASE
            WHEN pc_numdoc REGEXP '[A-Za-z]' THEN 'El numero (numdoc) contiene letras'
            ELSE 'El numero (numdoc) contiene caracteres especiales'
        END
    FROM commerce
    WHERE pc_processdate = p_process_date
      AND pc_numdoc IS NOT NULL
      AND TRIM(pc_numdoc) <> ''
      AND pc_numdoc NOT REGEXP '^[0-9]+$'
      AND NOT (pc_nomcomred IS NULL OR TRIM(pc_nomcomred) = '');

    SET v_count = v_count + ROW_COUNT();

    -- Eliminar de commerce los registros inválidos del día
    DELETE FROM commerce
    WHERE pc_processdate = p_process_date
      AND (
            pc_nomcomred IS NULL OR TRIM(pc_nomcomred) = ''
         OR pc_numdoc IS NULL OR TRIM(pc_numdoc) = ''
         OR pc_numdoc NOT REGEXP '^[0-9]+$'
      );

    SET p_quarantine_count = v_count;
END$$

DELIMITER ;
