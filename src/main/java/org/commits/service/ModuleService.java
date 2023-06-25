package org.commits.service;

import org.commits.entities.Module;
import org.commits.entities.commits.Commit;
import org.commits.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository repository;
    public Module getRoot() {
        return repository.getRoot();
    }

    public Module findModuleByPath(String path) {
        return repository.findByPath(path);
    }

    public List<String> getModuleChildrenNames(Module module) {
        return repository.getModuleChildrenNames(module);
    }

    public List<String> getModuleChildrenNames(String path) {
        return repository.getModuleChildrenNames(path);
    }

    public List<Module> getModule(Module module) {
        return repository.getModule(module);
    }

    public List<Module> getModuleChildren(String path) {
        return repository.getModuleChildren(path);
    }

    public void addCommit(Commit commit) {
        repository.addCommit(commit);
    }

}
