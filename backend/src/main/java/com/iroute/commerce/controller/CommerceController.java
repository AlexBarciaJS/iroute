package com.iroute.commerce.controller;

import com.iroute.commerce.dto.ProcessRequest;
import com.iroute.commerce.dto.ProcessResponse;
import com.iroute.commerce.dto.QuarantineResponse;
import com.iroute.commerce.dto.UploadResponse;
import com.iroute.commerce.service.CommerceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/commerce")
public class CommerceController {

    private final CommerceService commerceService;

    public CommerceController(CommerceService commerceService) {
        this.commerceService = commerceService;
    }

    /**
     * Carga el archivo commerce_DDMMYYYY.csv y registra cada fila mediante sp_create_commerce.
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(commerceService.uploadCommerceCsv(file));
    }

    /**
     * Procesa los comercios de una fecha (pc_processdate) y mueve inválidos a cuarentena.
     */
    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> process(@Valid @RequestBody ProcessRequest request) {
        return ResponseEntity.ok(commerceService.processByDate(request.getProcessDate()));
    }

    /**
     * Lista los registros de commerce_quarantine con su motivo.
     */
    @GetMapping("/quarantine")
    public ResponseEntity<List<QuarantineResponse>> listQuarantine() {
        return ResponseEntity.ok(commerceService.listQuarantine());
    }
}
