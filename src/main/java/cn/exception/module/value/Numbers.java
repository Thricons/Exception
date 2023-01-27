package cn.exception.module.value;

import java.util.function.Supplier;

public class Numbers<T extends Number> extends Value<T> {
    public T min;
    public T max;
    public T inc;
    public boolean dragged;
    private boolean isInteger;

    public Numbers(String name, T value, T min, T max, T inc) {
        super(name, value, () -> true);
        this.handleInteger(inc);
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public Numbers(String name, T value, T min, T max, T inc, Supplier<Boolean> visitable) {
        super(name, value, visitable);
        this.handleInteger(inc);
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    private void handleInteger(T inc) {
        if (inc.doubleValue() % 1 == 0.0) this.isInteger = true;
    }

    @Override
    public void setValue(Number value) {
        if (value.doubleValue() > max.doubleValue()) value = max;
        if (value.doubleValue() < min.doubleValue()) value = min;
        super.setValue((T) value);
    }

    public int intValue() {
        return this.getValue().intValue();
    }

    public float floatValue() {
        return this.getValue().floatValue();
    }

    public T getMinimum() {
        return this.min;
    }

    public T getMaximum() {
        return this.max;
    }

    public void setIncrement(T inc) {
        this.inc = inc;
    }

    public T getIncrement() {
        return this.inc;
    }

    public boolean isInteger() {
        return isInteger;
    }
}

