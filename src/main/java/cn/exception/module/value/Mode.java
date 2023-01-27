package cn.exception.module.value;

import java.util.function.Supplier;
public class Mode extends Value<String> {
    public boolean open;
    private boolean show = false;
    private final String[] modes;

    public Mode(String name, String[] modes, String value) {
        super(name, value, () -> true);
        this.modes = modes;
    }

    public Mode(String name, String[] modes, String value, Supplier<Boolean> visitable) {
        super(name, value, visitable);
        this.modes = modes;
    }
    public String[] getModes() {
        return this.modes;
    }

    public void setMode(String mode) {
        for (String s : this.modes) {
            if (s.equalsIgnoreCase(mode)) this.setValue(s);
        }
    }

    public void setMode(int mode) {
        this.setValue(this.modes[mode]);
    }

    public boolean isValid(String name) {
        for (String s : this.modes) {
            if (s.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public boolean isCurrentMode(String mode) {
        return this.getValue().equalsIgnoreCase(mode);
    }

    public boolean isCurrentModes(String mode) {
        return this.getValue().toLowerCase().contains(mode.toLowerCase());
    }

    public boolean isShow() {
        return this.show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}

