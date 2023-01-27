package cn.exception.event;

import cn.exception.Exception;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Event {

    private boolean cancelled;
    private Type type;

    public Event(Type type) {
        this.type = type;
        this.cancelled = false;
    }

    public enum Type {
        PRE, POST
    }

    public void call() {
        cancelled = false;

        CopyOnWriteArrayList<Data> dataList = Exception.instance.eventManager.get(this.getClass());

        if (dataList == null)
            return;

        dataList.forEach(data -> {
            
            try {
                data.getTarget().invoke(data.getSource(), this);
            } catch (IllegalAccessException | InvocationTargetException e) {
               // e.printStackTrace();
            }

        });


    }

    public Type getType() {
        return type;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


}