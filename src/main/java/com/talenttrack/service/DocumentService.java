package com.talenttrack.service;


import com.talenttrack.entities.Document;
import com.talenttrack.entities.User;
import com.talenttrack.repository.DocumentRepository;
import enums.DocumentType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    public Document saveDocumentForCurrentUser(MultipartFile file, DocumentType documentType) throws IOException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String filename = currentUser.getId() + "_" + documentType.name() + "_" + file.getOriginalFilename();

        Document document = new Document();
        document.setNom(filename);
        document.setType(documentType);
        document.setUser(currentUser);

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        return documentRepository.save(document);
    }

    public Resource getDocumentResourceById(Long documentId) throws IOException {
        Optional<Document> optionalDocument = documentRepository.findById(documentId);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            Path filePath = Paths.get(uploadDirectory).resolve(document.getNom());
            byte[] fileBytes = Files.readAllBytes(filePath);
            return new ByteArrayResource(fileBytes);
        } else {
            throw new FileNotFoundException("Document not found with id: " + documentId);
        }
    }

    @Transactional
    public void deleteDocument(Long documentId) throws IOException {
        Optional<Document> optionalDocument = documentRepository.findById(documentId);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            Path filePath = Paths.get(uploadDirectory).resolve(document.getNom());

            Files.deleteIfExists(filePath);

            documentRepository.delete(document);
        } else {
            throw new FileNotFoundException("Document not found with id: " + documentId);
        }
    }

    public Map<String, Long> getDocumentCounts() {
        List<Object[]> documentCounts = documentRepository.getDocumentCounts();
        Map<String, Long> countsMap = new HashMap<>();

        for (DocumentType documentType : DocumentType.values()) {
            countsMap.put(documentType.toString(), 0L);
        }

        for (Object[] row : documentCounts) {
            DocumentType documentType = (DocumentType) row[0];
            Long count = (Long) row[1];
            countsMap.put(documentType.toString(), count);
        }

        return countsMap;
    }


    public Resource downloadDocumentsAsZip(List<Long> documentIds) throws IOException {
        List<Document> documents = documentRepository.findAllById(documentIds);
        if (documents.isEmpty()) {
            throw new FileNotFoundException("No documents found with the provided IDs.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Document document : documents) {
                Path filePath = Paths.get(uploadDirectory).resolve(document.getNom());
                if (Files.exists(filePath)) {
                    addToZipFile(filePath.toFile(), zos);
                }
            }
        }

        return new ByteArrayResource(baos.toByteArray());
    }

    private void addToZipFile(File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        fis.close();
        zos.closeEntry();
    }
}
