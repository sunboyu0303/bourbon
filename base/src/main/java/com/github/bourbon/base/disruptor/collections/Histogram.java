package com.github.bourbon.base.disruptor.collections;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.DoubleUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 18:24
 */
public final class Histogram {
    /**
     * tracks the upper intervals of each of the buckets/bars
     */
    private final long[] upperBounds;
    /**
     * tracks the count of the corresponding bucket
     */
    private final long[] counts;
    /**
     * minimum value so far observed
     */
    private long minValue = Long.MAX_VALUE;
    /**
     * maximum value so far observed
     */
    private long maxValue = 0L;

    public Histogram(long[] upperBounds) {
        validateBounds(upperBounds);
        this.upperBounds = Arrays.copyOf(upperBounds, upperBounds.length);
        this.counts = new long[upperBounds.length];
    }

    private void validateBounds(long[] upperBounds) {
        long lastBound = -1L;
        if (upperBounds.length <= 0) {
            throw new IllegalArgumentException("Must provide at least one interval");
        }
        for (long bound : upperBounds) {
            if (bound <= 0L) {
                throw new IllegalArgumentException("Bounds must be positive values");
            }
            if (bound <= lastBound) {
                throw new IllegalArgumentException("bound " + bound + " is not greater than " + lastBound);
            }
            lastBound = bound;
        }
    }

    public int getSize() {
        return upperBounds.length;
    }

    public long getUpperBoundAt(int index) {
        return upperBounds[index];
    }

    public long getCountAt(int index) {
        return counts[index];
    }

    public boolean addObservation(long value) {
        int low = 0;
        int high = upperBounds.length - 1;
        // do a classic binary search to find the high value
        while (low < high) {
            int mid = low + ((high - low) >> 1);
            if (upperBounds[mid] < value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        // if the binary search found an eligible bucket, increment
        if (value <= upperBounds[high]) {
            counts[high]++;
            trackRange(value);
            return true;
        }
        // otherwise value was not found
        return false;
    }

    private void trackRange(long value) {
        if (value < minValue) {
            minValue = value;
        }
        if (value > maxValue) {
            maxValue = value;
        }
    }

    public void addObservations(Histogram histogram) {
        // validate the intervals
        if (upperBounds.length != histogram.upperBounds.length) {
            throw new IllegalArgumentException("Histograms must have matching intervals");
        }
        for (int i = 0, size = upperBounds.length; i < size; i++) {
            if (upperBounds[i] != histogram.upperBounds[i]) {
                throw new IllegalArgumentException("Histograms must have matching intervals");
            }
        }
        // increment all of the internal counts
        for (int i = 0, size = counts.length; i < size; i++) {
            counts[i] += histogram.counts[i];
        }
        // refresh the minimum and maximum observation ranges
        trackRange(histogram.minValue);
        trackRange(histogram.maxValue);
    }

    public void clear() {
        maxValue = 0L;
        minValue = Long.MAX_VALUE;
        for (int i = 0, size = counts.length; i < size; i++) {
            counts[i] = 0L;
        }
    }

    public long getCount() {
        long count = 0L;
        for (long l : counts) {
            count += l;
        }
        return count;
    }

    public long getMin() {
        return minValue;
    }

    public long getMax() {
        return maxValue;
    }

    public BigDecimal getMean() {
        // early exit to avoid divide by zero later
        if (0L == getCount()) {
            return BigDecimal.ZERO;
        }
        // pre calculate the initial lower bound; needed in the loop
        long lowerBound = counts[0] > 0L ? minValue : 0L;
        // use BigDecimal to avoid precision errors
        BigDecimal total = BigDecimal.ZERO;
        // midpoint is calculated as the average between the lower and upper bound (after taking into account the min & max values seen) then, simply multiply midpoint by the count of values at the interval (intervalTotal) and add to running total (total)
        for (int i = 0, size = upperBounds.length; i < size; i++) {
            if (0L != counts[i]) {
                long upperBound = Math.min(upperBounds[i], maxValue);
                long midPoint = lowerBound + ((upperBound - lowerBound) / 2L);
                total = total.add(new BigDecimal(midPoint).multiply(new BigDecimal(counts[i])));
            }
            // and recalculate the lower bound for the next time around the loop
            lowerBound = Math.max(upperBounds[i] + 1L, minValue);
        }
        return total.divide(new BigDecimal(getCount()), 2, RoundingMode.HALF_UP);
    }

    public long getFiftyUpperBound() {
        return getUpperBoundForFactor(0.50d);
    }

    public long getNinetyUpperBound() {
        return getUpperBoundForFactor(0.90d);
    }

    public long getTwoNinesUpperBound() {
        return getUpperBoundForFactor(0.99d);
    }

    public long getThreeNinesUpperBound() {
        return getUpperBoundForFactor(0.999d);
    }

    public long getFourNinesUpperBound() {
        return getUpperBoundForFactor(0.9999d);
    }

    public long getFiveNinesUpperBound() {
        return getUpperBoundForFactor(0.99999d);
    }

    public long getSixNinesUpperBound() {
        return getUpperBoundForFactor(0.999999d);
    }

    public long getSevenNinesUpperBound() {
        return getUpperBoundForFactor(0.9999999d);
    }

    public long getUpperBoundForFactor(double factor) {
        DoubleUtils.checkInRange(factor, 0.0d, 1.0d, "factor");
        long totalCount = getCount();
        long tailTotal = totalCount - Math.round(totalCount * factor);
        long tailCount = 0L;
        for (int i = counts.length - 1; i >= 0; i--) {
            if (0L != counts[i]) {
                tailCount += counts[i];
                if (tailCount >= tailTotal) {
                    return upperBounds[i];
                }
            }
        }
        return 0L;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Histogram").append(CharConstants.LEFT_BRACES);
        sb.append("min=").append(getMin()).append(StringConstants.COMMA_SPACE);
        sb.append("max=").append(getMax()).append(StringConstants.COMMA_SPACE);
        sb.append("mean=").append(getMean()).append(StringConstants.COMMA_SPACE);
        sb.append("50%=").append(getFiftyUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("90%=").append(getNinetyUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99%=").append(getTwoNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99.9%=").append(getThreeNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99.99%=").append(getFourNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99.999%=").append(getFiveNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99.9999%=").append(getSixNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append("99.99999%=").append(getSevenNinesUpperBound()).append(StringConstants.COMMA_SPACE);
        sb.append(CharConstants.LEFT_BRACKETS);
        for (int i = 0, size = counts.length; i < size; i++) {
            sb.append(upperBounds[i]).append(CharConstants.EQUAL).append(counts[i]).append(StringConstants.COMMA_SPACE);
        }
        if (counts.length > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(CharConstants.RIGHT_BRACKETS);
        sb.append(CharConstants.RIGHT_BRACES);
        return sb.toString();
    }
}