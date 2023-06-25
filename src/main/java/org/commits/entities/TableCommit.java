package org.commits.entities;

import jakarta.persistence.*;

public class TableCommit {

    enum CommitType{
        Creation,
        Deletion,
        Changer
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_path")
    private String fullPath;
    @Column(name = "commit_type")
    @Enumerated(EnumType.STRING)
    private CommitType commitType;
    @Column(name = "data")
    private String data;

    public void setId(Long id) {
        this.id = id;
    }
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
    public void setData(String data) { this.data = data; }

    public void setCommitType(CommitType commitType) {
        this.commitType = commitType;
    }
    public Long getId() {
        return id;
    }
    public String getFullPath() {
        return fullPath;
    }
    public CommitType getCommitType() {
        return commitType;
    }
    public String getData() { return data; }
}
