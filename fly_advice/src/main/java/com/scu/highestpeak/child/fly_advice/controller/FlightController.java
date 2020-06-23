package com.scu.highestpeak.child.fly_advice.controller;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.DTO.WhenFlyDTO;
import com.scu.highestpeak.child.fly_advice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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

    @GetMapping("/when")
    public Object findWhenFly(WhenFlyDTO whenFlyDTO){
        return flightService.predictPrice(whenFlyDTO);
    }

    @GetMapping("/cabinClass")
    @Cacheable(cacheNames = {"cabinClass"})
    public List<String> cabinClassValues() {
        return Arrays.stream(CABIN_CLASS.values()).map(Enum::name).collect(Collectors.toList());
    }

}
