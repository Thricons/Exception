package cn.exception;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;

/**
 * Code by MiLiBlue, At 2023/1/14
 **/
public class Loader extends Thread{
    public static byte[][] classes;

    private static Class tryGetClass(PrintWriter writer, ClassLoader cl, String... names) throws ClassNotFoundException {
        ClassNotFoundException lastException = null;
        for (String name : names) {
            try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(writer);
                lastException = e;
            }
        }
        throw lastException;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(System.getProperty("user.home") + File.separator + "eloader-log.txt", "UTF-8");
            writer.println("Starting!");
            writer.flush();
            try {
                ClassLoader cl = null;
                for (Thread thread : Thread.getAllStackTraces().keySet()) {
                    ClassLoader threadLoader;
                    if (thread == null || thread.getContextClassLoader() == null || (threadLoader = thread.getContextClassLoader()).getClass() == null || threadLoader.getClass().getName() == null) {
                        continue;
                    }
                    String loaderName = threadLoader.getClass().getName();
                    writer.println("Thread: " + thread.getName() + " [" + loaderName + "]");
                    writer.flush();
                    if (!loaderName.contains("LaunchClassLoader") && !loaderName.contains("RelaunchClassLoader")) {
                        continue;
                    }
                    cl = threadLoader;
                    break;
                }
                if (cl == null) {
                    throw new java.lang.Exception("ClassLoader is null");
                }
                this.setContextClassLoader(cl);
                Class forgeEventHandlerAnnotation = tryGetClass(writer, cl, "cpw.mods.fml.common.Mod$EventHandler", "net.minecraftforge.fml.common.Mod$EventHandler");
                Class modAnnotation = tryGetClass(writer, cl, "cpw.mods.fml.common.Mod", "net.minecraftforge.fml.common.Mod");
                Method loadMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
                loadMethod.setAccessible(true);
                writer.println("Loading " + classes.length + " classes");
                writer.flush();
                ArrayList<Object[]> mods = new ArrayList<>();
                for (byte[] classData : classes) {
                    if (classData == null) {
                        throw new java.lang.Exception("classData is null");
                    }
                    if (cl.getClass() == null) {
                        throw new java.lang.Exception("getClass() is null");
                    }
                    try {
                        Class tClass = (Class)loadMethod.invoke(cl, null, classData, 0, classData.length, cl.getClass().getProtectionDomain());
                        if (tClass.getAnnotation(modAnnotation) == null) {
                            continue;
                        }
                        Object[] mod = new Object[3];
                        mod[0] = tClass;
                        ArrayList<Method> fmlInitMethods = new ArrayList<Method>();
                        for (Method m : tClass.getDeclaredMethods()) {
                            if (m.getAnnotation(forgeEventHandlerAnnotation) != null && m.getParameterCount() == 1) {
                                m.setAccessible(true);
                                fmlInitMethods.add(m);
                            }
                        }
                        mod[2] = fmlInitMethods;
                        mods.add(mod);
                    }
                    catch (java.lang.Exception e) {
                        e.printStackTrace();
                        throw new java.lang.Exception("Exception on defineClass", e);
                    }
                }
                writer.println(classes.length + " loaded successfully");
                writer.flush();
                for (Object[] mod : mods) {
                    Class modClass = (Class) mod[0];
                    ArrayList<Method> fmlInitMethods = (ArrayList<Method>) mod[2];
                    Object modInstance = null;

                    try {
                        writer.println("Instancing " + modClass.getName());
                        writer.flush();
                        modInstance = modClass.newInstance();
                        writer.println("Instanced");
                        writer.flush();
                    }
                    catch (java.lang.Exception e) {
                        writer.println("Genexeption on instancing: " + e);
                        e.printStackTrace(writer);
                        writer.flush();
                        throw new java.lang.Exception("Exception on instancing", e);
                    }

                    for (Method initMethod : fmlInitMethods) {
                        try {
                            writer.println("Initing " + initMethod);
                            writer.flush();
                            writer.println("Inited");
                            writer.flush();
                            initMethod.invoke(modInstance, new Object[]{null});
                        }
                        catch (InvocationTargetException e) {
                            writer.println("InvocationTargetException on initing: " + e);
                            e.getCause().printStackTrace(writer);
                            writer.flush();
                            throw new java.lang.Exception("Exception on initing (InvocationTargetException)", e.getCause());
                        }
                        catch (java.lang.Exception e) {
                            writer.println("Genexeption on initing: " + e);
                            e.printStackTrace(writer);
                            writer.flush();
                            throw new java.lang.Exception("Exception on initing", e);
                        }
                    }
                }
                writer.println("Successfully injected");
                writer.flush();
            }
            catch (Throwable e) {
                e.printStackTrace(writer);
                writer.flush();
            }
            writer.close();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static int injectCP(byte[][] classes) {
        try {
            Loader.classes = classes;
            Loader t = new Loader();
            t.start();
        }
        catch (java.lang.Exception t) {
            // empty catch block
        }
        return 0;
    }

    public static byte[][] getByteArray(int size) {
        return new byte[size][];
    }
}
