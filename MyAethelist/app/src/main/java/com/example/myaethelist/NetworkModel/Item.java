package com.example.myaethelist.NetworkModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {
    Integer id;
    Integer category_id;
    String item_name;
    boolean finished;
    Double priority;
    boolean enable_notification;
    String notify_time;
    boolean enable_time_range;
    String due_time;
    String location;
    ArrayList<String> tags;
    String description;
    ArrayList<String> attachment_list;
    Item(){}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }

    public boolean isEnable_notification() {
        return enable_notification;
    }

    public void setEnable_notification(boolean enable_notification) {
        this.enable_notification = enable_notification;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public boolean isEnable_time_range() {
        return enable_time_range;
    }

    public void setEnable_time_range(boolean enable_time_range) {
        this.enable_time_range = enable_time_range;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAttachment_list() {
        return attachment_list;
    }

    public void setAttachment_list(ArrayList<String> attachment_list) {
        this.attachment_list = attachment_list;
    }
}
