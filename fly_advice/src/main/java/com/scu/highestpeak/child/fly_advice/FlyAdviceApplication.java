package com.scu.highestpeak.child.fly_advice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @author highestpeak
 */
@SpringBootApplication(
        exclude = {HibernateJpaAutoConfiguration.class}
)
@MapperScan("com.scu.highestpeak.child.fly_advice.orm")
public class FlyAdviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlyAdviceApplication.class, args);
    }

}
