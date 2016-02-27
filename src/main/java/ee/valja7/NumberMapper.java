package ee.valja7;

/**
 * Created by valeri on 27.02.16.
 */
public class NumberMapper {
    private final Double fromMin;
    private final Double fromMax;
    private final Double toMin;
    private final Double toMax;
    private Double fromDelta;
    private Double toDelta;
    private Double K;
    private Double Kback;

    public NumberMapper(Double fromMin, Double fromMax, Double toMin, Double toMax) {
        this.fromMin = fromMin;
        this.fromMax = fromMax;
        this.toMin = toMin;
        this.toMax = toMax;
        fromDelta = fromMax - fromMin;
        toDelta = toMax - toMin;
        Kback = fromDelta / toDelta;
        K = toDelta / fromDelta;
    }

    public Double Convert(Double value) {
        return toMin + (value - fromMin) * K;
    }

    public Double ConvertBack(Double value) {
        return fromMin + (value - toMin) * Kback;
    }

}
