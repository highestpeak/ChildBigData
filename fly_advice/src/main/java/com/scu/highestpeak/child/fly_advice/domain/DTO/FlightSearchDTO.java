package com.scu.highestpeak.child.fly_advice.domain.DTO;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author highestpeak
 */
public class FlightSearchDTO {
    @NotBlank(message = "必须提供出发地")
    private String startPlace;
    @NotBlank(message = "必须提供目的地")
    private String endPlace;

    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    private Date startDate;

    private CABIN_CLASS cabinClass;

    private Boolean rtn;
    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    private Date rtnDate;

    private Boolean preferDirects;
    private Boolean preferStop;
    private Boolean outboundAltEnabled;
    private Boolean inboundAltEnabled;

    @Positive
    private Integer remainingVotes;

    public FlightSearchDTO(String startPlace, String endPlace, Date startDate, CABIN_CLASS cabinClass, boolean rtn,
                           Date rtnDate, boolean preferDirects, boolean preferStop, boolean outboundAltEnabled,
                           boolean inboundAltEnabled, int remainingVotes) {
        if (startPlace.equals(endPlace)) {
            throw new RuntimeException("出发地和返回地不能相同");
        }
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.startDate = startDate;

        if (cabinClass == null) {
            throw new RuntimeException("必须提供舱位类型");
        }
        this.cabinClass = cabinClass;

        // todo: 添加异常到全局 exceptionhandle 类处
        if ((rtn) && (rtnDate == null)) {
            throw new RuntimeException("必须提供返程日期");
        }
        if (!rtnDate.after(startDate)) {
            throw new RuntimeException("出发日期必须早于返回日期");
        }
        this.rtn = rtn;
        this.rtnDate = rtnDate;
        this.preferDirects = preferDirects;
        this.preferStop = preferStop;
        this.outboundAltEnabled = outboundAltEnabled;
        this.inboundAltEnabled = inboundAltEnabled;
        this.remainingVotes = remainingVotes;
    }

    public FlightSearchDTO setStartPlace(String startPlace) {
        this.startPlace = startPlace;
        return this;
    }

    public FlightSearchDTO setEndPlace(String endPlace) {
        this.endPlace = endPlace;
        return this;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public Date getStartDate() {
        return startDate;
    }

    public CABIN_CLASS getCabinClass() {
        return cabinClass;
    }

    public Boolean getRtn() {
        return rtn;
    }

    public Date getRtnDate() {
        return rtnDate;
    }

    public Boolean getPreferDirects() {
        return preferDirects;
    }

    public Boolean getPreferStop() {
        return preferStop;
    }

    public Boolean getOutboundAltEnabled() {
        return outboundAltEnabled;
    }

    public Boolean getInboundAltEnabled() {
        return inboundAltEnabled;
    }

    public Integer getRemainingVotes() {
        return remainingVotes;
    }

    @Override
    public String toString() {
        return "FlightSearchDTO{" +
                "startPlace='" + startPlace + '\'' +
                ", endPlace='" + endPlace + '\'' +
                ", startDate=" + startDate +
                ", cabinClass=" + cabinClass +
                ", rtn=" + rtn +
                ", rtnDate=" + rtnDate +
                ", preferDirects=" + preferDirects +
                ", preferStop=" + preferStop +
                ", outboundAltEnabled=" + outboundAltEnabled +
                ", inboundAltEnabled=" + inboundAltEnabled +
                ", remainingVotes=" + remainingVotes +
                '}';
    }
}
