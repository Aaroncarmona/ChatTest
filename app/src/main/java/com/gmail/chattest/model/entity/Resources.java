package com.gmail.chattest.model.entity;

import com.gmail.chattest.constant.ConstantDb;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity( tableName = ConstantDb.TABLE_RESOURCES)
public class Resources {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String localPath;
    private String remotePath;
    private String type;
    private boolean remote;

    private int progress;
    private int size;

    public Resources(){

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Ignore
    public Resources(String name, String localPath, String type, boolean remote) {
        this.name = name;
        this.localPath = localPath;
        this.type = type;
        this.remote = remote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }
}
