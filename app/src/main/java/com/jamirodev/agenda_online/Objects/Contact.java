package com.jamirodev.agenda_online.Objects;

public class Contact {

    String id_contact, uid_contact, name, lastname, mail, phone, age, home, image;

    public Contact() {

    }

    public Contact(String id_contact, String uid_contact, String name, String lastname, String mail, String phone, String age, String home, String image) {
        this.id_contact = id_contact;
        this.uid_contact = uid_contact;
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.phone = phone;
        this.age = age;
        this.home = home;
        this.image = image;
    }

    public String getId_contact() {
        return id_contact;
    }

    public void setId_contact(String id_contact) {
        this.id_contact = id_contact;
    }

    public String getUid_contact() {
        return uid_contact;
    }

    public void setUid_contact(String uid_contact) {
        this.uid_contact = uid_contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
