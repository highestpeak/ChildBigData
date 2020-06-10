package com.highestpeakscu.child_bigdata_smallpractice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;

@SpringBootApplication
@MapperScan("com.highestpeakscu.child_bigdata_smallpractice.dao.mybatis_mapper")
public class ChildBigdataSmallpracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChildBigdataSmallpracticeApplication.class, args);
    }

}
