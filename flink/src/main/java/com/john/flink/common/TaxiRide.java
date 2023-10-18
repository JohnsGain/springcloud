package com.john.flink.common;

import lombok.Data;
import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author zhangjuwa
 * @apiNote
 * @date 2023-10-19 00:41
 * @since jdk17
 */
@Data
public class TaxiRide implements Serializable, Comparable<TaxiRide> {

    public long rideId;
    public boolean isStart;
    public Instant eventTime;
    public float startLon;
    public float startLat;
    public float endLon;
    public float endLat;
    public short passengerCnt;
    public long taxiId;
    public long driverId;


    /**
     * Creates a new TaxiRide with now as start and end time.
     */
    public TaxiRide() {
        this.eventTime = Instant.now();
    }

    /**
     * Invents a TaxiRide.
     */
    public TaxiRide(long rideId, boolean isStart) {
        DataGenerator g = new DataGenerator(rideId);

        this.rideId = rideId;
        this.isStart = isStart;
        this.eventTime = isStart ? g.startTime() : g.endTime();
        this.startLon = g.startLon();
        this.startLat = g.startLat();
        this.endLon = g.endLon();
        this.endLat = g.endLat();
        this.passengerCnt = g.passengerCnt();
        this.taxiId = g.taxiId();
        this.driverId = g.driverId();
    }

    /**
     * Compares this TaxiRide with the given one.
     *
     * <ul>
     *   <li>sort by timestamp,
     *   <li>putting START events before END events if they have the same timestamp
     * </ul>
     */
    public int compareTo(@Nullable TaxiRide other) {
        if (other == null) {
            return 1;
        }
        int compareTimes = this.eventTime.compareTo(other.eventTime);
        if (compareTimes == 0) {
            if (this.isStart == other.isStart) {
                return 0;
            } else {
                if (this.isStart) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            return compareTimes;
        }
    }

    @Override
    public String toString() {

        return rideId
                + ","
                + (isStart ? "START" : "END")
                + ","
                + eventTime.toString()
                + ","
                + startLon
                + ","
                + startLat
                + ","
                + endLon
                + ","
                + endLat
                + ","
                + passengerCnt
                + ","
                + taxiId
                + ","
                + driverId;
    }


    /**
     * Gets the ride's time stamp as a long in millis since the epoch.
     */
    public long getEventTimeMillis() {
        return eventTime.toEpochMilli();
    }

    /**
     * Gets the distance from the ride location to the given one.
     */
    public double getEuclideanDistance(double longitude, double latitude) {
        if (this.isStart) {
            return GeoUtils.getEuclideanDistance(
                    (float) longitude, (float) latitude, this.startLon, this.startLat);
        } else {
            return GeoUtils.getEuclideanDistance(
                    (float) longitude, (float) latitude, this.endLon, this.endLat);
        }
    }

    @VisibleForTesting
    public StreamRecord<TaxiRide> asStreamRecord() {
        return new StreamRecord<>(this, this.getEventTimeMillis());
    }

    @VisibleForTesting
    public StreamRecord<Long> idAsStreamRecord() {
        return new StreamRecord<>(this.rideId, this.getEventTimeMillis());
    }

}
