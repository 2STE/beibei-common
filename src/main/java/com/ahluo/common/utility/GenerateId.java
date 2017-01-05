package com.ahluo.common.utility;

/**
 * @author luochao . 2017-01-05.
 */
public class GenerateId {

    private long ahluoEpoch = 1483658565l;// LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

    private long defaultSeq = 0L;
    private long defaultDuration = 1L;
    private volatile long seq = defaultSeq;
    private long seqBits = 12L;
    private long seqMask = -1L ^ (-1L << seqBits);
    private long srvBits = 4;
    private long srvMask = 3;
    private long timeBits = seqBits + srvBits;
    private volatile long lastTimestamp = -1L;

    private long getTime() {
        return System.currentTimeMillis();
    }

    private long getNextTime(long lastTimestamp) {
        long currentTime = this.getTime();
        if (currentTime <= lastTimestamp) {
            currentTime = this.getTime();
        }
        return currentTime;
    }

    public synchronized long getNewId(long serviceType) {
        return getSeq() | serviceType & srvMask;
    }

    private long getSeq() {
        long currentTimestamp = getTime();
        if (this.lastTimestamp == currentTimestamp) {
            this.seq = (this.seq + defaultDuration) & this.seqMask;
            if (this.seq == defaultSeq) {
                currentTimestamp = getNextTime(this.lastTimestamp);
            }
        } else {
            seq = defaultSeq;
        }
        this.lastTimestamp = currentTimestamp;
        return currentTimestamp << timeBits | seq << srvBits;
    }

}
