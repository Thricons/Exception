package cn.exception.module.value;



import java.util.function.Supplier;

public class Value<V> {
    private String name;
    private V value;
    private final Supplier<Boolean> visitable;

    public Value(String name, V value, Supplier<Boolean> visitable) {
        this.name = name;
        this.visitable = visitable;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isVisitable() {
        return visitable.get();
    }
}
