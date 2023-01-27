package cn.exception.module.value;



import java.util.function.Supplier;

public class Option extends Value<Boolean> {

    public Option(String name, Boolean enabled) {
        super(name, enabled, () -> true);
    }

    public Option(String name, Boolean enabled, Supplier<Boolean> visitable) {
        super(name, enabled, visitable);
    }

    @Override
    public Boolean getValue() {
        return this.isVisitable() && super.getValue();
    }
}
