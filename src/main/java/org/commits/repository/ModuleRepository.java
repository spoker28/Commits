package org.commits.repository;
import org.commits.Model;
import org.commits.data_base.DataBase;
import org.commits.entities.Module;
import org.commits.entities.commits.Commit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ModuleRepository {
    private Model model = new Model();
    private DataBase dataBase = new DataBase();

    ModuleRepository () {
        model.changeSnapshot(dataBase.getCommitsFromFile());
    }

    public Module getRoot() {
        return model.getRoot();
    }

    public Module findByPath(String path) {
        Module module = model.getModule(path);
        if (module != null) {
            return module;
        }
        return null;
    }

    public List<String> getModuleChildrenNames(String path) {
        return getModuleChildrenNames(model.getModule(path));
    }

    public List<String> getModuleChildrenNames(Module module) {
        ArrayList<String> names = new ArrayList<String>();
        for (Map.Entry<String, Module> entry : module.getChilds().entrySet()) {
           names.add(entry.getKey());
        }
        return names;
    }

    public List<Module> getModuleChildren(String path) {
        return getModule(model.getModule(path));
    }

    public List<Module> getModule(Module module) {
        ArrayList<Module> names = new ArrayList<Module>();
        for (Map.Entry<String, Module> entry : module.getChilds().entrySet()) {
            names.add(entry.getValue());
        }
        return names;
    }

    public void addCommit(Commit commit) {
        dataBase.createFileFromCommit(commit);
        model.changeSnapshot(commit);
    }
}
