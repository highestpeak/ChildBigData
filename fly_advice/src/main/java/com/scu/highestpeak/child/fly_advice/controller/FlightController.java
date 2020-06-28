package com.scu.highestpeak.child.fly_advice.controller;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.DTO.WhenFlyDTO;
import com.scu.highestpeak.child.fly_advice.service.FlightService;
import com.scu.highestpeak.child.fly_advice.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author highestpeak
 */
@RestController
@RequestMapping("/api/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;
    @Autowired
    private HistoryService historyService;

    @GetMapping("/")
    public Object search(FlightSearchDTO flightSearchArgs) {
        Object result;
        try {
            flightSearchArgs.assertSourceDestinationNotEqual();
            flightSearchArgs.assertRtnDateRight();
            flightSearchArgs.assertCabinClassRight();
            result = flightService.searchFlight(flightSearchArgs);
        }catch (RuntimeException e){
            // todo: 这里应当只捕获 service 一层的 exception，
            //  strategy的exception就不应该捕获了
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    /**
     * 发送 机场名，时间起止范围
     * 返回这段日期的历史 日价格
     * 返回这段日期的历史 周价格
     * 下一版本：返回预测数据
     * @param whenFlyDTO
     * @return
     */
    @GetMapping("/when")
    public Object findWhenFly(WhenFlyDTO whenFlyDTO){
        return flightService.predictPrice(whenFlyDTO);
    }

    @GetMapping("/where")
    public Object findWhereFly(
            @NotBlank String  airport,
            @DateTimeFormat(pattern = "yyyyMMdd")@NotBlank Date start){
        return flightService.whereFlySearch(airport,start);
    }

    @GetMapping("/history/data")
    public Object historyOfSpecificFlight(@NotBlank String src,@NotBlank String dst){
        return historyService.historyOfSpecificFlight(src,dst);
    }

    @GetMapping("/cabinClass")
    @Cacheable(cacheNames = {"cabinClass"})
    public List<String> cabinClassValues() {
        return Arrays.stream(CABIN_CLASS.values()).map(Enum::name).collect(Collectors.toList());
    }

}
