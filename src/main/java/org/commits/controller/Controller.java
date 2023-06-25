package org.commits.controller;

import org.commits.entities.Module;
import org.commits.entities.commits.ChangerCommit;
import org.commits.entities.commits.CreationCommit;
import org.commits.entities.commits.DeletionCommit;
import org.commits.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class Controller {

    @Autowired
    private ModuleService service;

    @GetMapping
    public ResponseEntity<List<Module>> findRoot() {
        List<Module> modules = service.getModule(service.getRoot());
        return modules != null && !modules.isEmpty()
                ? new ResponseEntity<>(modules, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{path}")
    public ResponseEntity<Module> findModule(@PathVariable(name = "path") String path) {
        if (path.startsWith("_")) {
            Module module = service.findModuleByPath(path);
            return module != null
                    ? new ResponseEntity<>(module, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> findRootChildrenNames() {
        List<String> list = service.getModuleChildrenNames(service.getRoot());
        return list != null && !list.isEmpty()
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/names/{path}")
    public ResponseEntity<List<String>> findRootChildren(@PathVariable(name = "path") String path) {
        if (path.startsWith("_")) {
            List<String> list = service.getModuleChildrenNames(path);
            return list != null && !list.isEmpty()
                    ? new ResponseEntity<>(list, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    @PostMapping("/creation")
    public ResponseEntity<?> createCreationCommit(@RequestBody CreationCommit commit) {
        service.addCommit(commit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/deletion")
    public ResponseEntity<?> createDeletionCommit(@RequestBody DeletionCommit commit) {
        service.addCommit(commit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/changer")
    public ResponseEntity<?> createChangerCommit(@RequestBody ChangerCommit commit) {
        service.addCommit(commit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
