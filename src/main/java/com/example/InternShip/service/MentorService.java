package com.example.InternShip.service;

import java.util.List;

public interface MentorService {

    Object getAll(List<Integer> department, String keyword, int page);
}
