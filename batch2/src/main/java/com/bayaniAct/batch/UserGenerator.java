package com.bayaniAct.batch;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

class User {
    String userId;
    String firstName = "User";
    String lastName = "Test";
    String middleName = "M";
    String birthDate = "1990-01-01";
    String gender = "Male";
    String address = "123 Main St";
    String contactNumber = "1234567890";
    String email;
    String validId = "Driver's License";
    String validIdNumber = "ID123456";
    String imageId = "image123.jpg";
    String birthPlace = "Cityville";
    String medicalHistory = "None";
    String occupation = "Engineer";
    String affiliation = "CompanyX";
    String nationality = "American";
    boolean isActive = true;
    String createdAt = "2024-02-14";
    String updatedAt = "2024-02-14";
    String status = "Active";
    String username;
    String password;
    String insertSQL;
    String insertSQL1;
    
    public User(int userId, String baseEmail) throws NoSuchAlgorithmException {
        this.userId = UUID.randomUUID().toString();
        this.email = baseEmail.replace("@", "+" + String.format("%03d", userId) + "@");
        this.username = "user" + userId;
        this.password = hashPassword("password" + userId);
        
        this.insertSQL = String.format(
            "INSERT INTO bayani_act.residents (user_id, first_name, last_name, middle_name, birth_date, gender, address, contact_number, email, valid_id, valid_id_number, image_id, birth_place, medical_history, occupation, affiliation, nationality, is_active, created_at, updated_at, status) " +
            "VALUES ('%s',  '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%b', '%s', '%s', '%s');",
            this.userId,  this.firstName, this.lastName, this.middleName, this.birthDate, this.gender, this.address, this.contactNumber, this.email, this.validId, this.validIdNumber, this.imageId, this.birthPlace, this.medicalHistory, this.occupation, this.affiliation, this.nationality, this.isActive, this.createdAt, this.updatedAt, this.status
        );
        this.insertSQL1 = String.format(
                "INSERT INTO bayani_act.users (user_id, username, password, enabled) " +
                "VALUES ('%s',  '%s', '%s', '%s');",
                this.userId,  this.username, this.password,0);
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}

public class UserGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of users to generate: ");
        int numUsers = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter the base email address: ");
        String baseEmail = scanner.nextLine();
        
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= numUsers; i++) {
            users.add(new User(i, baseEmail));
        }
        
        writeToExcel(users);
        System.out.println("Excel file created successfully!");
        scanner.close();
    }

    public static void writeToExcel(List<User> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet userInfoSheet = workbook.createSheet("UserInfo");
        Sheet userAuthSheet = workbook.createSheet("UserAuth");
        Sheet sqlSheet = workbook.createSheet("SQLStatements");
        
        String[] userInfoHeaders = {"user_id", "first_name", "last_name", "middle_name", "birth_date", "gender", "address", "contact_number", "email", "valid_id", "valid_id_number", "image_id", "birth_place", "medical_history", "occupation", "affiliation", "nationality", "is_active", "created_at", "updated_at", "status"};
        String[] userAuthHeaders = {"user_id", "username", "password"};
        
        Row headerRow = userInfoSheet.createRow(0);
        for (int i = 0; i < userInfoHeaders.length; i++) {
            headerRow.createCell(i).setCellValue(userInfoHeaders[i]);
        }
        
        Row authHeaderRow = userAuthSheet.createRow(0);
        for (int i = 0; i < userAuthHeaders.length; i++) {
            authHeaderRow.createCell(i).setCellValue(userAuthHeaders[i]);
        }
        
        int rowIdx = 1;
        for (User user : users) {
            Row row = userInfoSheet.createRow(rowIdx);
            row.createCell(0).setCellValue(user.userId);
            row.createCell(1).setCellValue(user.firstName);
            row.createCell(2).setCellValue(user.lastName);
            row.createCell(3).setCellValue(user.middleName);
            row.createCell(4).setCellValue(user.birthDate);
            row.createCell(5).setCellValue(user.gender);
            row.createCell(6).setCellValue(user.address);
            row.createCell(7).setCellValue(user.contactNumber);
            row.createCell(8).setCellValue(user.email);
            row.createCell(9).setCellValue(user.validId);
            row.createCell(10).setCellValue(user.validIdNumber);
            row.createCell(11).setCellValue(user.imageId);
            row.createCell(12).setCellValue(user.birthPlace);
            row.createCell(13).setCellValue(user.medicalHistory);
            row.createCell(14).setCellValue(user.occupation);
            row.createCell(15).setCellValue(user.affiliation);
            row.createCell(16).setCellValue(user.nationality);
            row.createCell(17).setCellValue(user.isActive);
            row.createCell(18).setCellValue(user.createdAt);
            row.createCell(19).setCellValue(user.updatedAt);
            row.createCell(20).setCellValue(user.status);
            
            Row rows = userAuthSheet.createRow(rowIdx);
            rows.createCell(0).setCellValue(user.userId);
            rows.createCell(1).setCellValue(user.username);
            rows.createCell(2).setCellValue(user.password);
            rows.createCell(3).setCellValue(0);
            
            Row sqlRow = sqlSheet.createRow(rowIdx);
            sqlRow.createCell(0).setCellValue(user.insertSQL);
            sqlRow.createCell(1).setCellValue(user.insertSQL1);
            rowIdx++;
        }
        
        try (FileOutputStream fileOut = new FileOutputStream("UserData.xlsx")) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}
