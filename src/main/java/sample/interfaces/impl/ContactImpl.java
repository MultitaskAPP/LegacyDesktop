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
                contact.setAvatar(new Image(new ImageTweakerTool(contact.getIdContact()).getProfilePicUser()));

                if (!(rawJSON.isNull("birthday")))
                    contact.setBirthday(Date.valueOf(rawJSON.getString("birthday")));

                if (!(rawJSON.isNull("tlf")))
                    contact.setTlf(rawJSON.getInt("tlf"));

                if (!(rawJSON.isNull("address")))
                    contact.setAddress(rawJSON.getString("address"));

                if (!(rawJSON.isNull("socialMedia")))
                    contact.setSocialMediaJSON(rawJSON.getJSONObject("socialMedia"));

                contactList.add(contact);

            }
        }

        return contactList;
    }
}
