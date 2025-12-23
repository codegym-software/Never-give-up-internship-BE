package com.example.InternShip.service;

import java.io.IOException;

public interface DataBackUpService {
    void backupDatabase() throws IOException, InterruptedException;
}
