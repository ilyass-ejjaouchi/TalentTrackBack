package com.talenttrack.service;

import com.talenttrack.entities.Role;
import com.talenttrack.entities.User;
import com.talenttrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    private final UserRepository userRepository;

    public byte[] exportUsersByRoleToExcel(Role role) throws IOException {
        List<User> users = userRepository.findByRole(role);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nom", "Prénom", "Matricule", "Titre", "Diplôme", "Etablissement", "Description Tâches", "Email"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getNom());
                row.createCell(2).setCellValue(user.getPrenom());
                row.createCell(3).setCellValue(user.getMatricule());
                row.createCell(4).setCellValue(user.getTitre());
                row.createCell(5).setCellValue(user.getDiplome());
                row.createCell(6).setCellValue(user.getEtablissement());
                row.createCell(7).setCellValue(user.getDescriptionTaches());
                row.createCell(8).setCellValue(user.getEmail());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
