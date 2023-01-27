package cn.exception.module.value;

public class Text extends Value<String>{
    public Text(String name, String value) {
        super(name, value, () -> true);
    }
}
