package sample.interfaces.impl;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IContact;
import sample.models.Contact;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactImpl implements IContact {

    @Override
    public List<Contact> getAllContacts() {

        List<Contact> contactList = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());

        ConnAPI connAPI = new ConnAPI("/api/users/contacts/readAll", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray dataJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < dataJSON.length(); i++){
                JSONObject rawJSON = dataJSON.getJSONObject(i);
                Contact contact = new Contact();

                contact.setIdContact(rawJSON.getInt("idUser"));
                contact.setName(rawJSON.getString("name"));
                contact.setFirstSurname(rawJSON.getString("firstSurname"));
                contact.setLastSurname(rawJSON.getString("lastSurname"));
                contact.setEmail(rawJSON.getString("email"));
                contact.setVersionAvatar(rawJSON.getInt("versionAvatar"));

                if (contact.getVersionAvatar() == 0){
                    contact.setAvatar(new Image(new ImageTweakerTool(contact.getIdContact()).getProfilePicUser()));
                }else{
                    contact.setAvatar(new Image(new ImageTweakerTool(contact.getIdContact(), contact.getVersionAvatar()).getProfilePicUser()));
                }

                if (!(rawJSON.isNull("birthday")))
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("birthday"));
                        contact.setBirthday(new Date(dateParsed.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                if (!(rawJSON.isNull("tlf")))
                    contact.setTlf(rawJSON.getInt("tlf"));

                if (!(rawJSON.isNull("address")))
                    contact.setAddress(rawJSON.getString("address"));

                if (!(rawJSON.isNull("socialMedia")))
                    contact.setSocialMediaJSON(new JSONObject(rawJSON.getString("socialMedia")));

                contactList.add(contact);

            }
        }

        return contactList;
    }

    @Override
    public boolean deleteContact(int idContact) {
        return false;
    }

    @Override
    public boolean addContact(int idContact) {
        return false;
    }

    @Override
    public boolean rejectFriendRequest(int idContact) {
        return false;
    }

    @Override
    public boolean acceptFriendRequest(int idContact) {
        return false;
    }
}
