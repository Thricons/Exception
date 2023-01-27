package cn.exception.manager;

import cn.exception.module.Module;
import cn.exception.module.impl.combat.AutoClicker;
import cn.exception.module.impl.movement.Eagle;
import cn.exception.module.impl.movement.Sprint;
import cn.exception.module.impl.player.FastPlace;
import cn.exception.module.impl.render.ChinaHat;
import cn.exception.module.impl.render.ClickGui;
import cn.exception.module.impl.render.HUD;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Code by MiLiBlue, At 2022/12/27
 **/
public class ModuleManager {
    public List<Module> moduleList = new ArrayList<>();

    public void loadModules(){
        moduleList.add(new HUD());
        moduleList.add(new AutoClicker());
        moduleList.add(new Sprint());
        moduleList.add(new FastPlace());
        moduleList.add(new ClickGui());
        moduleList.add(new Eagle());
        moduleList.add(new ChinaHat());
    }

    public Module getModuleByName(String s){
        for (Module m : moduleList) {
            if (!m.getName().equalsIgnoreCase(s)) {
                continue;
            }
            return m;
        }
        return null;
    }

    public Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : moduleList) {
            if (m.getClass() != cls) {
                continue;
            }
            return m;
        }
        return null;
    }

    public List<Module> getModulesInType(Module.Category t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : moduleList) {
            if (m.getCategroy() != t) {
                continue;
            }
            output.add(m);
        }
        return output;
    }

    public ArrayList<Module> getEnabledModList() {
        ArrayList<Module> enabledModList = new ArrayList();
        for (Module m : moduleList) {
            if (m.isEnable()) {
                enabledModList.add(m);
            }
        }
        return enabledModList;
    }

    /**public void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = Anxious.instance.moduleManager.getModuleByName(name);
            if (m == null) continue;
            if(bind == null){
                JOptionPane.showConfirmDialog(null, "Null!", "Null!", 1);
                continue;
            }
            m.setKey(Integer.parseInt(bind));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = Anxious.instance.moduleManager.getModuleByName(v);
            if (m == null) continue;
            m.setEnable(true);
        }
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = Anxious.instance.moduleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }**/
}