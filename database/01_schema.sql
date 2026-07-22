-- i-Route: esquema de base de datos
CREATE DATABASE IF NOT EXISTS iroutedb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE iroutedb;

DROP TABLE IF EXISTS commerce_quarantine;
DROP TABLE IF EXISTS commerce;

-- Estructura alineada al CSV commerce_DDMMYYYY.csv
CREATE TABLE commerce (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pc_codcom       VARCHAR(20)  NOT NULL,
    pc_nomcomred    VARCHAR(100) NULL,
    pc_tipdoc       VARCHAR(5)   NULL,
    pc_numdoc       VARCHAR(20)  NULL,
    pc_processdate  DATE         NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_commerce_processdate (pc_processdate)
) ENGINE=InnoDB;

CREATE TABLE commerce_quarantine (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pc_codcom       VARCHAR(20)  NOT NULL,
    pc_nomcomred    VARCHAR(100) NULL,
    pc_tipdoc       VARCHAR(5)   NULL,
    pc_numdoc       VARCHAR(20)  NULL,
    pc_processdate  DATE         NOT NULL,
    motivo          VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_quarantine_processdate (pc_processdate)
) ENGINE=InnoDB;
