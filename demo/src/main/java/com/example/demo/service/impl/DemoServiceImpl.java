package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateDemoRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.DemoMap;
import com.example.demo.repository.DemoRepository;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoRepository demoRepository;
    @Autowired
    private DemoMap demoMap;

    public String createDemo(CreateDemoRequest request){
        try {
            demoRepository.save(demoMap.createDemo(request));
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DUPLICATE_DATA);
        }
        return "ok";
    }
}
