package com.example.InternShip.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.InternShip.dto.cloudinary.response.FileResponse;
import com.example.InternShip.entity.Allowance;
import com.example.InternShip.entity.AllowancePackage;
import com.example.InternShip.entity.Intern;
import com.example.InternShip.entity.InternshipProgram;
import com.example.InternShip.entity.MonthlyAllowanceReport;
import com.example.InternShip.repository.AllowanceRepository;
import com.example.InternShip.repository.AttendanceRepository;
import com.example.InternShip.repository.InternRepository;
import com.example.InternShip.repository.MonthlyAllowanceReportRepository;
import com.example.InternShip.service.AllowanceCalculationService;
import com.example.InternShip.service.AllowancePackageService;
import com.example.InternShip.service.AllowanceReportService;
import com.example.InternShip.service.CloudinaryService;

import jakarta.transaction.Transactional;

@Service
public class AllowanceCalculationServicelmpl implements AllowanceCalculationService {

    @Autowired
    private AllowancePackageService allowancePackageService;
    @Autowired
    private InternRepository internRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AllowanceRepository allowanceRepository;
    @Autowired
    private AllowanceReportService allowanceReportService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private MonthlyAllowanceReportRepository monthlyAllowanceReportRepository;

    @Scheduled(cron = "0 10 16 * * ?")
    @Transactional
    public void calculateMonthlyAllowances() {
        YearMonth yearMonth = YearMonth.now().minusMonths(1);
        System.out.println("========== START calculateMonthlyAllowances ==========");
        System.out.println("Input YearMonth = " + yearMonth);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        System.out.println("StartDate = " + startDate);
        System.out.println("EndDate   = " + endDate);

        List<Intern> activeInterns = internRepository.findAllByStatus(Intern.Status.ACTIVE);
        System.out.println("Active interns count = " + activeInterns.size());

        if (activeInterns.isEmpty()) {
            System.out.println("No active interns found for allowance calculation.");
            return;
        }

        // =================== COUNT WORK DAYS ===================
        System.out.println("----- Counting work days for each intern -----");

        Map<Integer, Long> workDaysMap = activeInterns.stream()
                .collect(Collectors.toMap(
                        Intern::getId,
                        intern -> {
                            Long days = attendanceRepository
                                    .countWorkDays(intern.getId(), startDate, endDate);

                            System.out.println(
                                    "Intern ID=" + intern.getId()
                                            + ", Name=" + intern.getUser().getFullName()
                                            + ", WorkDays=" + days);

                            return days;
                        }));

        System.out.println("WorkDaysMap size = " + workDaysMap.size());

        List<Allowance> newlyGeneratedAllowances = new ArrayList<>();

        // =================== MAIN LOOP ===================
        for (Intern intern : activeInterns) {

            System.out.println("\n---- Processing intern ----");
            System.out.println("Intern ID=" + intern.getId());
            System.out.println("Intern Name=" + intern.getUser().getFullName());

            InternshipProgram program = intern.getInternshipProgram();

            if (program == null) {
                System.out.println("❌ Intern has NO internship program → SKIP");
                continue;
            }

            System.out.println("Program ID=" + program.getId());
            System.out.println("Program Name=" + program.getName());

            List<AllowancePackage> activePackages = allowancePackageService
                    .findActiveAllowancePackageEntitiesByProgramId(program.getId());

            System.out.println("Active allowance packages count = "
                    + (activePackages == null ? 0 : activePackages.size()));

            if (activePackages == null || activePackages.isEmpty()) {
                System.out.println("❌ No active allowance packages → SKIP intern");
                continue;
            }

            long actualWorkDays = workDaysMap.getOrDefault(intern.getId(), 0L);
            System.out.println("Actual work days = " + actualWorkDays);

            for (AllowancePackage pkg : activePackages) {

                System.out.println("  -> Checking package ID=" + pkg.getId());
                System.out.println("     RequiredWorkDays=" + pkg.getRequiredWorkDays());
                System.out.println("     Amount=" + pkg.getAmount());

                if (actualWorkDays >= pkg.getRequiredWorkDays()) {

                    System.out.println("     ✅ Eligible for allowance");

                    Allowance allowance = new Allowance();
                    allowance.setIntern(intern);
                    allowance.setAllowancePackage(pkg);
                    allowance.setAmount(pkg.getAmount());
                    allowance.setStatus(Allowance.Status.PENDING);
                    allowance.setAllowanceMonth(startDate);
                    allowance.setPeriod(startDate);
                    allowance.setWorkDays((int) actualWorkDays);

                    Allowance savedAllowance = allowanceRepository.save(allowance);

                    System.out.println("     💾 Allowance SAVED");
                    System.out.println("        Allowance ID=" + savedAllowance.getId());
                    System.out.println("        Intern ID=" + intern.getId());
                    System.out.println("        Package ID=" + pkg.getId());

                    newlyGeneratedAllowances.add(savedAllowance);

                } else {
                    System.out.println("     ❌ Not eligible ("
                            + actualWorkDays + " < " + pkg.getRequiredWorkDays() + ")");
                }
            }
        }

        System.out.println("\nTotal newly generated allowances = "
                + newlyGeneratedAllowances.size());

        // =================== REPORT ===================
        if (!newlyGeneratedAllowances.isEmpty()) {
            try {
                System.out.println("----- Generating allowance report -----");

                byte[] reportBytes = allowanceReportService.generateAllowanceReport(yearMonth);

                System.out.println("Report generated, bytes = "
                        + (reportBytes == null ? 0 : reportBytes.length));

                String time = LocalDateTime.now().toString();

                String fileName = "danh_sach_intern_tro_cap_thang_" + time + ".xlsx";

                System.out.println("Uploading report to Cloudinary...");
                System.out.println("FileName = " + fileName);

                FileResponse fileResponse = cloudinaryService.uploadFile_Month_allowance_report(
                        reportBytes,
                        fileName,
                        "allowance-reports");

                System.out.println("Upload SUCCESS");
                System.out.println("File URL = " + fileResponse.getFileUrl());

                MonthlyAllowanceReport report = new MonthlyAllowanceReport();
                report.setReportMonth(startDate);
                report.setFileName(fileName);
                report.setFileUrl(fileResponse.getFileUrl());

                monthlyAllowanceReportRepository.save(report);

                System.out.println("MonthlyAllowanceReport SAVED to DB");

                System.out.println("Successfully generated and uploaded allowance summary report for "
                        + time);

            } catch (IOException e) {
                System.err.println("❌ ERROR generating/uploading report for "
                        + yearMonth);
                e.printStackTrace();
            }
        } else {
            System.out.println("No allowance generated → SKIP report");
        }

        System.out.println("========== END calculateMonthlyAllowances ==========");
    }

}