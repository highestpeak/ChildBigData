package com.scu.highestpeak.child.fly_advice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.scu.highestpeak.child.fly_advice.dao")
public class FlyAdviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAdviceApplication.class, args);
    }

}
