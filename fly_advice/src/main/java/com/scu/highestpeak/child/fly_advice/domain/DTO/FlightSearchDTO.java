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
    private Boolean inboundAltEnabled;

    @NotBlank(message = "必须提供目的地")
    private String endPlace;
    private Boolean outboundAltEnabled;

    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    private Date startDate;

    private CABIN_CLASS cabinClass;

    private Boolean rtn;
    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    private Date rtnDate;

    private Boolean preferDirects;
    /**
     * todo: 经停处理
     */
    private Boolean preferStop;
    /**
     * future: 暂时默认 转机一次
     */
    private Boolean preferTransfer;

    @Positive
    private Integer remainingVotes;

    public FlightSearchDTO() {
    }

    public FlightSearchDTO(String startPlace, String endPlace, Date startDate, CABIN_CLASS cabinClass, boolean rtn,
                           Date rtnDate, boolean preferDirects, boolean preferStop, boolean preferTransfer,
                           boolean outboundAltEnabled,
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
        this.preferTransfer = preferTransfer;
        this.outboundAltEnabled = outboundAltEnabled;
        this.inboundAltEnabled = inboundAltEnabled;
        this.remainingVotes = remainingVotes;
    }

    public FlightSearchDTO setInboundAltEnabled(Boolean inboundAltEnabled) {
        this.inboundAltEnabled = inboundAltEnabled;
        return this;
    }

    public FlightSearchDTO setOutboundAltEnabled(Boolean outboundAltEnabled) {
        this.outboundAltEnabled = outboundAltEnabled;
        return this;
    }

    public FlightSearchDTO setCabinClass(CABIN_CLASS cabinClass) {
        this.cabinClass = cabinClass;
        assertCabinClassRight();
        return this;
    }

    public FlightSearchDTO setRtn(Boolean rtn) {
        this.rtn = rtn;
        return this;
    }

    public FlightSearchDTO setRtnDate(Date rtnDate) {
        this.rtnDate = rtnDate;
        assertRtnDateRight();
        return this;
    }

    public FlightSearchDTO setPreferDirects(Boolean preferDirects) {
        this.preferDirects = preferDirects;
        return this;
    }

    public FlightSearchDTO setPreferStop(Boolean preferStop) {
        this.preferStop = preferStop;
        return this;
    }

    public FlightSearchDTO setPreferTransfer(Boolean preferTransfer) {
        this.preferTransfer = preferTransfer;
        return this;
    }

    public FlightSearchDTO setRemainingVotes(Integer remainingVotes) {
        this.remainingVotes = remainingVotes;
        return this;
    }

    public FlightSearchDTO setStartPlace(String startPlace) {
        this.startPlace = startPlace.toUpperCase();
        assertSourceDestinationNotEqual();
        return this;
    }

    public FlightSearchDTO setEndPlace(String endPlace) {
        this.endPlace = endPlace.toUpperCase();
        assertSourceDestinationNotEqual();
        return this;
    }

    public FlightSearchDTO setStartDate(Date startDate) {
        this.startDate = startDate;
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

    public Boolean getOutboundAltEnabled() {
        return outboundAltEnabled;
    }

    public Boolean getInboundAltEnabled() {
        return inboundAltEnabled;
    }

    public Boolean getRtn() {
        return rtn;
    }

    public Boolean getPreferDirects() {
        return preferDirects;
    }

    public Boolean getPreferStop() {
        return preferStop;
    }

    public Boolean getPreferTransfer() {
        return preferTransfer;
    }

    public Integer getRemainingVotes() {
        return remainingVotes;
    }

    public void switchSourceDestination() {
        String tmp = this.startPlace;
        this.setStartPlace(endPlace);
        this.setEndPlace(tmp);
    }

    /**
     * todo: 防错处理
      */
    public void assertSourceDestinationNotEqual() {
        if (startPlace == null || endPlace == null) {
            return;
        }
        if (startPlace.equals(endPlace)) {
            throw new RuntimeException("出发地和返回地不能相同");
        }
    }

    public void assertRtnDateRight() {
        if (rtn == null) {
            return;
        }
        if (rtn){
            if (startDate==null){
                throw new RuntimeException("必须提供出发日期");
            }
            if (rtnDate == null) {
                throw new RuntimeException("必须提供返程日期");
            }
            if (!rtnDate.after(startDate)) {
                throw new RuntimeException("出发日期必须早于返回日期");
            }
        }
    }

    public void assertCabinClassRight() {
        if (cabinClass == null) {
            throw new RuntimeException("必须提供舱位类型");
        }
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
