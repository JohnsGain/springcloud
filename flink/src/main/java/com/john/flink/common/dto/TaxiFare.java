package com.john.flink.common.dto;

import com.john.flink.common.DataGenerator;
import lombok.Data;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-11-10 01:02
 * @since jdk17
 */
@Data
public class TaxiFare implements Serializable {

    private static final long serialVersionUID = 5245620296803930093L;


    public TaxiFare() {
        this.startTime = Instant.now();
    }

    public TaxiFare(long rideId) {
        DataGenerator g = new DataGenerator(rideId);

        this.rideId = rideId;
        this.taxiId = g.taxiId();
        this.driverId = g.driverId();
        this.startTime = g.startTime();
        this.paymentType = g.paymentType();
        this.tip = g.tip();
        this.tolls = g.tolls();
        this.totalFare = g.totalFare();
    }

    public TaxiFare(
            long rideId,
            long taxiId,
            long driverId,
            Instant startTime,
            String paymentType,
            float tip,
            float tolls,
            float totalFare) {
        this.rideId = rideId;
        this.taxiId = taxiId;
        this.driverId = driverId;
        this.startTime = startTime;
        this.paymentType = paymentType;
        this.tip = tip;
        this.tolls = tolls;
        this.totalFare = totalFare;
    }

    private long rideId;
    private long taxiId;
    private long driverId;
    private Instant startTime;
    private String paymentType;
    private float tip;
    private float tolls;
    private float totalFare;

    public long getEventTimeMillis() {
        return this.startTime.toEpochMilli();
    }

    @VisibleForTesting
    public StreamRecord<TaxiFare> asStreamRecord() {
        return new StreamRecord<>(this, getEventTimeMillis());
    }

}
