package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.enums.Role;
import com.example.sprintjabackend.model.Package;
import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.repository.PackageRepository;
import com.example.sprintjabackend.repository.UserRepository;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class ReportsGenerator {

    private   final UserRepository userRepository;
    private final PackageRepository packageRepository;

    @Autowired
    public ReportsGenerator(UserRepository userRepository, PackageRepository packageRepository) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    public void generateExcel(HttpServletResponse response) throws Exception {

        List<User> userList = userRepository.findAllByRoleOrderByLastNameAsc(Role.ROLE_USER.toString());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sprint JA Users Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("TRN");
        row.createCell(1).setCellValue("FirstName");
        row.createCell(2).setCellValue("LastName");
        row.createCell(3).setCellValue("Email");
        row.createCell(4).setCellValue("PhoneNumber");
        row.createCell(5).setCellValue("Pick-up Branch");
        row.createCell(6).setCellValue("Street Address");
        row.createCell(7).setCellValue("Parish");

        int dataRowIndex = 1;

        for (User user: userList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(user.getTrn());
            dataRow.createCell(1).setCellValue(user.getFirstName());
            dataRow.createCell(2).setCellValue(user.getLastName());
            dataRow.createCell(3).setCellValue(user.getEmail());
            dataRow.createCell(4).setCellValue(user.getPhoneNumber());
            dataRow.createCell(5).setCellValue(user.getPickUpBranch());
            dataRow.createCell(6).setCellValue(user.getStreetAddress());
            dataRow.createCell(7).setCellValue(user.getParish());
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }


    public void generateExcelPackages(HttpServletResponse response) throws Exception {

        List<Package> packageList = packageRepository.findAllByOrderByCreatedAtAsc();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sprint JA Users Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Tracking Number");
        row.createCell(2).setCellValue("Weight");
        row.createCell(3).setCellValue("Cost");


        int dataRowIndex = 1;

        for (Package aPackage: packageList) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(aPackage.getFirstName()+" "+ aPackage.getLastName());
            dataRow.createCell(1).setCellValue(aPackage.getTrackingNumber());
            dataRow.createCell(2).setCellValue(aPackage.getWeight());
            dataRow.createCell(3).setCellValue(aPackage.getCost());
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
