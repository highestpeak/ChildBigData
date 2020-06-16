package com.scu.highestpeak.child.fly_advice.controller;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import com.scu.highestpeak.child.fly_advice.domain.DTO.FlightSearchDTO;
import com.scu.highestpeak.child.fly_advice.domain.DTO.WhenFlyDTO;
import com.scu.highestpeak.child.fly_advice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        // future: 使用 spring-boot-starter-validation 来构建联合验证
        return flightService.searchFlight(flightSearchArgs);
    }

    @GetMapping("/when")
    public Object findWhenFly(WhenFlyDTO whenFlyDTO){
        return null;
    }

    @GetMapping("/cabinClass")
    @Cacheable(cacheNames = {"cabinClass"})
    public List<String> cabinClassValues() {
        return Arrays.stream(CABIN_CLASS.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GetMapping("/prefix")
    @Cacheable(cacheNames = {"prefixAirport"}, key = "#prefix")
    public List<String> prefixAirport(@RequestParam(name = "RequestParam") String prefix) {
        return flightService.airportContainsSubstr(prefix, GlobalStaticFactory.SUBSTR_PREFIX);
    }

}
