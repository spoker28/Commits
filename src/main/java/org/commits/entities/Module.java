package org.commits.entities;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Module {
    private String fullName;
    private String name;
    private String data;
    private HashMap<String, Module> childs;

    public void setData(String data) {
        this.data = data;
    }
    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public HashMap<String, Module> getChilds() {
        return childs;
    }
    public Module (String fullName, String name, String data) {
        this.fullName = fullName;
        this.name = name;
        this.data = data;
        childs = new HashMap<>();
    }

    public Module() {
        fullName = "";
        name = "";
        data = "";
        childs = new HashMap<>();
    }

    public void printModuleAndChildrenToConsole() {
        printModuleToConsole();
        System.out.print("\t");
        for (Map.Entry<String, Module> entry: childs.entrySet()) {
            entry.getValue().printModuleAndChildrenToConsole("\t");
        }
    }

    private void printModuleAndChildrenToConsole(String t) {
        printModuleToConsole();
        t += "\t";
        for (Map.Entry<String, Module> entry: childs.entrySet()) {
            System.out.print(t);
            entry.getValue().printModuleAndChildrenToConsole(t);
        }
    }

    private void printModuleToConsole() {
        System.out.println("file " + fullName + " with data - " + data + "\n");
    }


}
