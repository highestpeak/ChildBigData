package com.scu.highestpeak.child.fly_advice.controller;

import com.scu.highestpeak.child.fly_advice.GlobalStaticFactory;
import com.scu.highestpeak.child.fly_advice.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author highestpeak
 */
@RestController
@RequestMapping("/api/airport")
public class AirportController {
    @Autowired
    private AirportService airportService;

    @GetMapping("/prefix")
    @Cacheable(cacheNames = {"prefixAirport"}, key = "#prefix")
    public List prefixAirport(@RequestParam(name = "name") String prefix) {
        return airportService.airportContainsSubstr(prefix, GlobalStaticFactory.SUBSTR_PREFIX);
    }

}
