package sample.models;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.sql.Date;

public class User {

    private int idUser;
    private String email, name, firstSurname, lastSurname;
    private Date birthday;
    private int tlf;
    private String pass;
    private Color colourUser;
    private Image avatarUser;
    private int versionAvatar;
    private JSONArray privacitySettings;
    private JSONObject socialMedia;
    private String address;

    public User(){}

    public User(int idUser, String email, String name, String firstSurname, String lastSurname, Date birthday, int tlf, String pass, Color colourUser, Image avatarUser, int versionAvatar, String address, JSONArray privacitySettings, JSONObject socialMedia) {
        this.idUser = idUser;
        this.email = email;
        this.name = name;
        this.firstSurname = firstSurname;
        this.lastSurname = lastSurname;
        this.birthday = birthday;
        this.tlf = tlf;
        this.pass = pass;
        this.colourUser = colourUser;
        this.avatarUser = avatarUser;
        this.versionAvatar = versionAvatar;
        this.privacitySettings = privacitySettings;
        this.socialMedia = socialMedia;
        this.address = address;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Color getColourUser() {
        return colourUser;
    }

    public void setColourUser(Color colourUser) {
        this.colourUser = colourUser;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Image getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(Image avatarUser) {
        this.avatarUser = avatarUser;
    }

    public int getVersionAvatar() {
        return versionAvatar;
    }

    public void setVersionAvatar(int versionAvatar) {
        this.versionAvatar = versionAvatar;
    }

    public JSONArray getPrivacitySettings() {
        return privacitySettings;
    }

    public void setPrivacitySettings(JSONArray privacitySettings) {
        this.privacitySettings = privacitySettings;
    }

    public JSONObject getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(JSONObject socialMedia) {
        this.socialMedia = socialMedia;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHexCode(){

        int r = colourUser.getRed();
        int g = colourUser.getGreen();
        int b = colourUser.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    @Override
    public String toString() {
        return name + " " + firstSurname + " " + lastSurname;
    }

    public JSONObject toJSONObject() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("firstSurname", firstSurname);
        jsonObject.put("lastSurname", lastSurname);
        jsonObject.put("birthday", birthday);
        jsonObject.put("tlf", tlf);
        jsonObject.put("socialMedia", socialMedia);
        jsonObject.put("address", address);

        return jsonObject;

    }
}
