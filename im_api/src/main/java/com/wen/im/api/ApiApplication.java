package com.wen.im.api;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.wen.im.api.mapper"})
@EnableTransactionManagement
public class ApiApplication
{
    private static final Logger log = LoggerFactory.getLogger(ApiApplication.class);
    public static void main( String[] args )
    {
        SpringApplication.run(ApiApplication.class);
    }
}

