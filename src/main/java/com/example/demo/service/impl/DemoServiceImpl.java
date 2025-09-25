package com.example.demo.service.impl;

import com.example.demo.dto.request.CreateDemoRequest;
import com.example.demo.entity.Demo;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.DemoRepository;
import com.example.demo.service.DemoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoRepository demoRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String createDemo(CreateDemoRequest request){
        try {
            demoRepository.save(modelMapper.map(request, Demo.class));
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.DUPLICATE_DATA);
        }
        return "ok";
    }
}
