package cn.exception.event.events;

import cn.exception.event.Event;

public class EventMove extends Event {
    public static double x;
    public static double y;
    public static double z;
    private double motionX;
    private double motionY;
    private double motionZ;

    public EventMove(double x, double y, double z) {
        super(Type.POST);
        EventMove.x = x;
        EventMove.y = y;
        EventMove.z = z;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        EventMove.x = x;
    }

    public double getY() {
        return y;
    }

    public static void setY(double y) {
        EventMove.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        EventMove.z = z;
    }

    public void zero() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }
}
