package io.logicco.gnotes.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    @NonNull
    private UUID id;

    private String title;

    private String content;

    private boolean archived;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public Note(String title) {
        Date currDate = new Date();
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = "";
        this.createdAt = currDate;
        this.updatedAt = currDate;
        this.archived = false;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setArchived(boolean isArchived){
        this.archived = isArchived;
    }

    public boolean isArchived(){
        return this.archived;
    }

}
