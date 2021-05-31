package sample.models;

import javafx.scene.image.Image;
import org.json.JSONObject;

import java.sql.Date;

public class Contact {

    private int idContact;
    private String name, firstSurname, lastSurname;
    private String email;
    private Date birthday;
    private int tlf;
    private String address;
    private JSONObject socialMediaJSON;
    private Image avatar;

    public Contact() {}

    public Contact(int idContact, String name, String firstSurname, String lastSurname, String email, Date birthday, int tlf, String address, JSONObject socialMediaJSON, Image avatar) {
        this.idContact = idContact;
        this.name = name;
        this.firstSurname = firstSurname;
        this.lastSurname = lastSurname;
        this.email = email;
        this.birthday = birthday;
        this.tlf = tlf;
        this.address = address;
        this.socialMediaJSON = socialMediaJSON;
        this.avatar = avatar;
    }

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getLastSurname() {
        return lastSurname;
    }

    public void setLastSurname(String lastSurname) {
        this.lastSurname = lastSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getTlf() {
        return tlf;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject getSocialMediaJSON() {
        return socialMediaJSON;
    }

    public void setSocialMediaJSON(JSONObject socialMediaJSON) {
        this.socialMediaJSON = socialMediaJSON;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return name + " " + firstSurname + " " + lastSurname;
    }
}
