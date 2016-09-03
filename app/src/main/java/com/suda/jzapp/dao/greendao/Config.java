package com.suda.jzapp.dao.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

/**
 * Entity mapped to table "CONFIG".
 */
@Entity
public class Config {

    @Id(autoincrement = true)
    private Long id;
    private String key;
    private String value;
    private String ObjectID;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;

    @Generated(hash = 589037648)
    public Config() {
    }

    public Config(Long id) {
        this.id = id;
    }

    @Generated(hash = 1087611119)
    public Config(Long id, String key, String value, String ObjectID, java.util.Date createdAt, java.util.Date updatedAt) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.ObjectID = ObjectID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getObjectID() {
        return ObjectID;
    }

    public void setObjectID(String ObjectID) {
        this.ObjectID = ObjectID;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public java.util.Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.util.Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    // KEEP METHODS - put your custom methods here
    public boolean getBooleanValue() {
        return "true".equals(getValue());
    }

    public void setBooleanValue(boolean value) {
        setValue(String.valueOf(value));
    }
    // KEEP METHODS END

}
