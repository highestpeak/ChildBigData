package com.scu.highestpeak.child.fly_advice.domain.DTO;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @author highestpeak
 */
public class FlightSearchDTO {
    /**
     * todo: 校验：提供的地点：机场完整三字码、机场完整名字
     *  不能在 这个类里做联合参数校验，必须在controller处校验数据，因为这个类的字段注入顺序是未知的
     *
     * todo: 选择供应商
     */
    @NotBlank(message = "必须提供出发地")
    private String startPlace;
    private Boolean inboundAltEnabled;

    @NotBlank(message = "必须提供目的地")
    private String endPlace;
    private Boolean outboundAltEnabled;

    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    @NotBlank
    private Date startDate;

    private CABIN_CLASS cabinClass;

    private Boolean rtn;
    @DateTimeFormat(pattern = "yyyyMMdd")
    @FutureOrPresent
    private Date rtnDate;

    private Boolean preferDirects;
    /**
     * future: 经停处理
     */
    private Boolean preferStop;
    /**
     * assume:暂时默认 转机一次
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
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.startDate = startDate;

        this.cabinClass = cabinClass;

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
        return this;
    }

    public FlightSearchDTO setRtn(Boolean rtn) {
        this.rtn = rtn;
        return this;
    }

    public FlightSearchDTO setRtnDate(Date rtnDate) {
        this.rtnDate = rtnDate;
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
        return this;
    }

    public FlightSearchDTO setEndPlace(String endPlace) {
        this.endPlace = endPlace.toUpperCase();
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

    public Date getRtnDate() {
        return rtnDate;
    }

    public CABIN_CLASS getCabinClass() {
        return cabinClass;
    }

    public Boolean getOutboundAltEnabled() {
        return outboundAltEnabled==null?false:outboundAltEnabled;
    }

    public Boolean getInboundAltEnabled() {
        return inboundAltEnabled==null?false:inboundAltEnabled;
    }

    public Boolean getRtn() {
        return rtn==null?false:rtn;
    }

    public Boolean getPreferDirects() {
        return preferDirects==null?false:preferDirects;
    }

    public Boolean getPreferStop() {
        return preferStop==null?false:preferStop;
    }

    public Boolean getPreferTransfer() {
        return preferTransfer==null?false:preferTransfer;
    }

    public Integer getRemainingVotes() {
        return remainingVotes==null?0:remainingVotes;
    }

    /**
     * 防错处理
      */
    public void assertSourceDestinationNotEqual() {
        if (startPlace.equals(endPlace)) {
            throw new RuntimeException("出发地和返回地不能相同");
        }
    }

    public void assertRtnDateRight() {
        if (rtn){
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
