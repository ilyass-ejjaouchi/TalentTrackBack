package com.talenttrack.controller;

import com.talenttrack.entities.Document;
import com.talenttrack.entities.User;
import com.talenttrack.repository.DocumentRepository;
import com.talenttrack.service.DocumentService;
import enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentRepository documentRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("documentType") DocumentType documentType) {
        try {
            documentService.saveDocumentForCurrentUser(file, documentType);
            return ResponseEntity.status(HttpStatus.CREATED).body("Document uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload document: " + e.getMessage());
        }
    }

    @GetMapping("/view/{documentId}")
    public ResponseEntity<Resource> viewDocument(@PathVariable Long documentId) {
        try {
            Resource fileResource = documentService.getDocumentResourceById(documentId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .header("X-Frame-Options", "SAMEORIGIN")
                    .body(fileResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(("Failed to retrieve document: " + e.getMessage()).getBytes()));
        }
    }

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long documentId) {
        try {
            documentService.deleteDocument(documentId);
            return ResponseEntity.ok("Document deleted successfully.");
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete document: " + e.getMessage());
        }
    }

    @GetMapping("/document-counts")
    public ResponseEntity<Map<String, Long>> getDocumentCounts() {
        Map<String, Long> countsMap = documentService.getDocumentCounts();
        return ResponseEntity.ok(countsMap);
    }

    @GetMapping("")
    public ResponseEntity<List<Document>> getUserDocuments(@RequestParam("documentType") DocumentType documentType) {
        List<Document> userDocuments;
            userDocuments = documentRepository.findByType(documentType);
        return ResponseEntity.ok(userDocuments);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadDocumentsAsZip(@RequestParam("documentIds") List<Long> documentIds) {
        try {
            Resource zipResource = documentService.downloadDocumentsAsZip(documentIds);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documents.zip\"")
                    .body(zipResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(("Failed to download documents: " + e.getMessage()).getBytes()));
        }
    }

    @GetMapping("/user-documents")
    public ResponseEntity<List<Document>> getUserDocuments() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Document> userDocuments = documentRepository.findByUser(currentUser);
        return ResponseEntity.ok(userDocuments);
    }

}
