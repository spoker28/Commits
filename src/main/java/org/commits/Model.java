package org.commits;

import org.commits.entities.commits.Commit;
import org.commits.entities.Module;

import java.util.ArrayList;
import java.util.Map;

public class Model {
    private Module root;

    public Model() {
        root = new Module();
    }

    public Module getRoot() {
        return root;
    }

    public void changeSnapshot(ArrayList<Commit> commits) {
        for (Commit i : commits) {
            changeSnapshot(i);
        }
    }

    public void changeSnapshot(Commit commit) {
        try {
            commit.applyCommit(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Module getModule(String path) {
        try {
            String nextDirs = path;
            int index = nextDirs.indexOf("_");
            Module parent = root;
            while (index != -1) {
                nextDirs = nextDirs.substring(index + 1);
                int tempIndex = nextDirs.indexOf("_");
                if (tempIndex == -1) {
                    break;
                }
                String currentDir = nextDirs.substring(0, tempIndex);
                boolean found = false;
                String key = null;
                for (Map.Entry<String, Module> entry : parent.getChilds().entrySet()) {
                    key = entry.getKey();
                    if (entry.getValue().getName().equals(currentDir)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new Exception("Can't find " + nextDirs + " file to edit with path" + path + ")");
                } else {
                    parent = parent.getChilds().get(key);
                }
                index = nextDirs.indexOf("_");
            }
            boolean found = false;
            String key = null;
            for (Map.Entry<String, Module> entry : parent.getChilds().entrySet()) {
                key = entry.getKey();
                if (entry.getValue().getName().equals(nextDirs)) {
                    found = true;
                    return parent.getChilds().get(key);
                }
            }
            throw new Exception("Can't find " + path + " file to edit");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
