package com.example.demo.mapper;

import com.example.demo.dto.request.CreateDemoRequest;
import com.example.demo.entity.Demo;
import org.springframework.stereotype.Component;

@Component
public class DemoMap {

    public Demo createDemo(CreateDemoRequest request) {
        Demo demo = new Demo();
        demo.setHoTen(request.getHoTen());
        demo.setEmail(request.getEmail());
        demo.setGioiTinh(request.getGioiTinh());
        demo.setNgaySinh(request.getNgaySinh());
        return demo;
    }
}
