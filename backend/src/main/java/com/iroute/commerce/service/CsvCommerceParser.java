package com.iroute.commerce.service;

import com.iroute.commerce.dto.CommerceRowDto;
import com.iroute.commerce.exception.BusinessException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvCommerceParser {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("ddMMyyyy")
    };

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("El archivo CSV no debe estar vacío", HttpStatus.BAD_REQUEST);
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new BusinessException("El nombre del archivo es inválido", HttpStatus.BAD_REQUEST);
        }

        String lowerName = originalName.toLowerCase(Locale.ROOT);
        if (!lowerName.endsWith(".csv")) {
            throw new BusinessException("El archivo debe ser un CSV (.csv)", HttpStatus.BAD_REQUEST);
        }

        if (!lowerName.matches("commerce_\\d{8}\\.csv")) {
            throw new BusinessException(
                    "El archivo debe denominarse commerce_DDMMYYYY.csv",
                    HttpStatus.BAD_REQUEST);
        }
    }

    public List<CommerceRowDto> parse(MultipartFile file) {
        validateFile(file);

        List<CommerceRowDto> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isBlank()) {
                throw new BusinessException("El archivo CSV no debe estar vacío", HttpStatus.BAD_REQUEST);
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                rows.add(parseLine(line, lineNumber));
            }
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException(
                    "No se pudo leer el archivo CSV: " + ex.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        if (rows.isEmpty()) {
            throw new BusinessException("El archivo CSV no debe estar vacío", HttpStatus.BAD_REQUEST);
        }

        return rows;
    }

    private CommerceRowDto parseLine(String line, int lineNumber) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new BusinessException(
                    "Línea " + lineNumber + ": se esperaban 5 columnas",
                    HttpStatus.BAD_REQUEST);
        }

        String codcom = trimToNull(parts[0]);
        if (codcom == null) {
            throw new BusinessException(
                    "Línea " + lineNumber + ": pc_codcom es obligatorio",
                    HttpStatus.BAD_REQUEST);
        }

        return new CommerceRowDto(
                codcom,
                trimToEmpty(parts[1]),
                trimToEmpty(parts[2]),
                trimToEmpty(parts[3]),
                parseDate(parts[4], lineNumber));
    }

    private LocalDate parseDate(String rawValue, int lineNumber) {
        String value = trimToNull(rawValue);
        if (value == null) {
            throw new BusinessException(
                    "Línea " + lineNumber + ": pc_processdate es obligatorio",
                    HttpStatus.BAD_REQUEST);
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
                // try next format
            }
        }

        throw new BusinessException(
                "Línea " + lineNumber + ": fecha inválida en pc_processdate (" + value + ")",
                HttpStatus.BAD_REQUEST);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
