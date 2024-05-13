package com.talenttrack.controller;

import com.talenttrack.entities.Role;
import com.talenttrack.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/export")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ExportController {
    private final ExportService exportService;

    @GetMapping("/users/{roleId}")
    public ResponseEntity<ByteArrayResource> exportUsersByRoleToExcel(@PathVariable("roleId") Role role) throws IOException {
        byte[] excelBytes = exportService.exportUsersByRoleToExcel(role);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users_by_role.xlsx");

        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
