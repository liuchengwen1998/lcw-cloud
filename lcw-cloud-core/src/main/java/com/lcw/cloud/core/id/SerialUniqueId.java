package com.lcw.cloud.core.id;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 在一个workerId内连续唯一的ID生成方法(绝对连续单调递增，全局唯一).Serial Unique Id in one workerid.
 * 优点:连续唯一;不依赖时钟. 在DB内实现,可达到分布式全局唯一ID在DB内自增长;在同一个workerId内,获取的ID号,可以满足连续单调递增唯一.
 *
 *  * <pre>{@code
 *  * +------+----------------------+----------+-----------+-----------+
 *  * | sign |     time(second)     | segment  | workerId  | sequence  |
 *  * +------+----------------------+----------+-----------+-----------+
 *  *   1 bit        31 bits           9 bits     10 bits     13 bits
 *  * }</pre>
 *
 */
public class SerialUniqueId implements GenId {

    private final AtomicLong sequenceNumber;

    private final long startTime; // second 31
    private static final long segment = 0L; // 3
    private static final long sequence = 1L; // 19

    private static final long timeBits = 31L;
    private static final long segmentBits = 3L;
    private static final long sequenceBits = 19L;

    private static final long workerIdShift = sequenceBits + segmentBits + timeBits;
    private static final long timestampLeftShift = sequenceBits + segmentBits;
    private static final long segmentShift = sequenceBits;

    private final long initNum;


    /**
     * !注意:每次新建都会重置开始号码.
     */
    public SerialUniqueId(long workerId) {
        if (workerId < 0 || workerId > 1023) throw new RuntimeException("配置全局唯一编号的机器Id范围得在 [0,1023]");
        startTime = _curSecond();
        // 单位：s    2021-01-01 (yyyy-MM-dd)
        long initEpoch = 1609430400;
        initNum = (workerId << workerIdShift) | ((startTime - initEpoch) << timestampLeftShift) | (segment << segmentShift) | (sequence);
        sequenceNumber = new AtomicLong(initNum);
    }

    private long _curSecond() {
        return (System.currentTimeMillis()) / 1000L;
    }

    @Override
    public synchronized long get() {
        long id = sequenceNumber.getAndIncrement();
        testSpeedLimit(id);
        return id;
    }

    @Override
    public synchronized long[] getRangeId(int sizeOfIds) {
        long[] r = new long[2];
        r[0] = sequenceNumber.getAndIncrement();
        r[1] = r[0] + sizeOfIds - 1;
        setSequenceNumber(r[0] + sizeOfIds);
        //使用乐观锁的话,会浪费ID号
        testSpeedLimit(r[1]);
        return r;
    }

    private void setSequenceNumber(long newNum) {
        sequenceNumber.set(newNum);
    }

    private synchronized void testSpeedLimit(long currentLong) {
        long spentTime = _curSecond() - startTime + 1;
        if (spentTime > 0) {
            if ((spentTime << timestampLeftShift) > (currentLong - initNum)) return;
        }
        try {
            wait(10);
            testSpeedLimit(currentLong);
        } catch (InterruptedException e) {
            throw new RuntimeException("获取全局唯一ID失败，", e);
        }
    }


    public static void main(String[] args) {
        final SerialUniqueId id = new SerialUniqueId(1);
        // 获取单个唯一Id
        System.out.println(id.get());
        // 获取批量Id
        System.out.println(Arrays.toString(id.getRangeId(10)));
    }

}