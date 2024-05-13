package com.talenttrack.repository;

import com.talenttrack.entities.Document;
import com.talenttrack.entities.User;
import enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUser(User user);

    @Query("SELECT d.type, COUNT(d) FROM Document d GROUP BY d.type")
    List<Object[]> getDocumentCounts();

    List<Document> findByType(DocumentType documentType);

}
