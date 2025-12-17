package com.example.InternShip.config.scheduler;

import com.example.InternShip.service.AllowanceCalculationService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class MonthlyAllowanceCalculationJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(MonthlyAllowanceCalculationJob.class);

    @Autowired
    private AllowanceCalculationService allowanceCalculationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("============== QUARTZ JOB: STARTING MONTHLY ALLOWANCE CALCULATION ==============");
        try {
            // Calculate allowances for the previous month
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            log.info("Calculating allowances for: {}", previousMonth);
            allowanceCalculationService.calculateMonthlyAllowances(previousMonth);
            log.info("============== QUARTZ JOB: COMPLETED MONTHLY ALLOWANCE CALCULATION for {} ==============", previousMonth);
        } catch (Exception e) {
            log.error("!!!!!!!!!!!!!! QUARTZ JOB: FAILED to execute MonthlyAllowanceCalculationJob !!!!!!!!!!!!!!", e);
            // Optionally re-throw as a JobExecutionException
            // throw new JobExecutionException(e);
        }
    }
}
