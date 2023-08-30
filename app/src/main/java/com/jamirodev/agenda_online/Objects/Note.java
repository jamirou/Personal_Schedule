package com.jamirodev.agenda_online.Objects;

public class Note {

    String id_note, uid_user, mail_user, date_actual_hour, title, description, date_note, state;

    public Note() {
    }

    public Note(String id_note, String uid_user, String mail_user, String date_actual_hour, String title, String description, String date_note, String state) {
        this.id_note = id_note;
        this.uid_user = uid_user;
        this.mail_user = mail_user;
        this.date_actual_hour = date_actual_hour;
        this.title = title;
        this.description = description;
        this.date_note = date_note;
        this.state = state;
    }

    public String getId_note() {
        return id_note;
    }

    public void setId_note(String id_note) {
        this.id_note = id_note;
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public String getMail_user() {
        return mail_user;
    }

    public void setMail_user(String mail_user) {
        this.mail_user = mail_user;
    }

    public String getDate_actual_hour() {
        return date_actual_hour;
    }

    public void setDate_actual_hour(String date_actual_hour) {
        this.date_actual_hour = date_actual_hour;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_note() {
        return date_note;
    }

    public void setDate_note(String date_note) {
        this.date_note = date_note;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
