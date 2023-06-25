package org.commits.entities.commits;

import org.commits.entities.Module;

public abstract class Commit {

    protected int id;
    protected String fullPath;

    public Commit() {}

    public Commit(int id, String path) {
        this.id = id;
        this.fullPath = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public int getId() {
        return id;
    }

    public String getFullPath() {
        return fullPath;
    }

    public abstract void applyCommit(Module root) throws Exception;

    public String getName() {
        int index = fullPath.lastIndexOf("_");
        if (index == -1) {
            return fullPath;
        }
        return fullPath.substring(index + 1);
    }

}
