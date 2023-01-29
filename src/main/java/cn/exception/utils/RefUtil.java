package cn.exception.utils;

import java.lang.reflect.Field;

/**
 * @author MiLiBlue
 **/
public class RefUtil {
    public static void set(Object o, Object v, String... fields){
        Class c = o.getClass();
        for (String s : fields){
            try {
                //Sexy
                Field f = c.getDeclaredField(s);
                f.setAccessible(true);
                f.set(o, v);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
