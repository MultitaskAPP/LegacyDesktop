package sample.models;

import java.util.Date;

public class User {

    private int idUser;
    private String email, name, firstSurname, lastSurname;
    private Date birthday;
    private int tlf;
    private String colourUser, pass;

    public User(){}

    public User(int idUser, String email, String name, String firstSurname, String lastSurname, Date birthday, int tlf, String colourUser, String pass) {
        this.idUser = idUser;
        this.email = email;
        this.name = name;
        this.firstSurname = firstSurname;
        this.lastSurname = lastSurname;
        this.birthday = birthday;
        this.tlf = tlf;
        this.colourUser = colourUser;
        this.pass = pass;
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

    public String getColourUser() {
        return colourUser;
    }

    public void setColourUser(String colourUser) {
        this.colourUser = colourUser;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return name + " " + firstSurname + " " + lastSurname;
    }
}
