package com.spring.todomanagement.common.util;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class DatabaseSupporter {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @AfterEach
    void cleanUp() {
        databaseCleaner.tableClear();
    }
}
