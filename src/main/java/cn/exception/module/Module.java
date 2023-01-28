package cn.exception.module;

import cn.exception.Exception;
import cn.exception.module.impl.render.HUD;
import cn.exception.module.value.Value;
import cn.exception.utils.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class Module {
    public String name;
    public int key;
    public boolean enable;
    public Category category;
    public String suffix;
    public List<Value> valueList = new ArrayList<>();
    protected Minecraft mc = Minecraft.getMinecraft();
    public float animY = 1;
    public float animX = 1;
    private float anWidth;
    public AnimationUtil animationUtil = new AnimationUtil();
    public AnimationUtil animationUtil2 = new AnimationUtil();
    //for Disabled Anim
    public boolean preDisable = false;

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public List<Value> getValues() {
        return valueList;
    }

    protected void addValues(Value... values) {
        Value[] var5 = values;
        int var4 = values.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            Value value = var5[var3];
            this.valueList.add(value);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        if(!enable && preDisable == true){
            preDisable = false;
            enable = true;
        }
        if (Exception.instance.moduleManager.moduleList.contains(HUD.class) && Exception.instance.moduleManager.getModuleByClass(HUD.class).isEnable() && enable == false) {
            preDisable = true;
        } else {
            this.enable = enable;
        }
        if(enable){
            animX = new ScaledResolution(mc).getScaledWidth();
            Exception.instance.eventManager.register(this);
            onEnable();
        }else {
            Exception.instance.eventManager.unregister(this);
            onDisable();
        }
    }

    public Category getCategroy() {
        return category;
    }

    public void setCategroy(Category category) {
        this.category = category;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void onEnable(){}

    public void onDisable(){}

    public List<Value> getValueList() {
        return valueList;
    }

    public float getAnimY() {
        return animY;
    }

    public void setAnimY(float animY) {
        this.animY = animY;
    }

    public float getAnimX() {
        return animX;
    }

    public void setAnimX(float animX) {
        this.animX = animX;
    }

    public float getAnwidth() {
        return anWidth;
    }

    public void setAnwidth(float anWidth) {
        this.anWidth = anWidth;
    }

    public enum Category {
        Combat,Movement,Player,Render,World,Elements
    }
}
