package org.commits.entities.commits;

import org.commits.entities.Module;

import java.util.Map;

//@Jacksonized
//@Builder
//@Data
public class ChangerCommit extends Commit {
    private String data;

    public ChangerCommit() {
        super();
    }

    public ChangerCommit(int id, String path, String data) {
        super(id, path);
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public void applyCommit(Module root) throws Exception {
        String nextDirs = getFullPath();
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
            for (Map.Entry<String, Module> entry: parent.getChilds().entrySet()) {
                key = entry.getKey();
                if (entry.getValue().getName().equals(currentDir)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Exception("Can't find " + nextDirs +  " file to edit with path " + getFullPath() + ")");
            } else {
                parent = parent.getChilds().get(key);
            }
            index = nextDirs.indexOf("_");
        }
        boolean found = false;
        String key = null;
        for (Map.Entry<String, Module> entry: parent.getChilds().entrySet()) {
            key = entry.getKey();
            if (entry.getValue().getName().equals(nextDirs)) {
                found = true;
                parent.getChilds().get(key).setData(data);
                break;
            }
        }
        if (!found) {
            throw new Exception("Can't find " + nextDirs + " file to edit");
        }
    }
}
