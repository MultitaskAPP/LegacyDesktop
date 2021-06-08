package sample.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * ImageTweakerTool - v1.0
 * Simple tool to edit images.
 * The user uses JFileChooser to choose the image, transforms it to 256x256 pixels,
 * 1:1 ratio and centers it. Finally it saves it in a local directory.
 *
 * @author Xavi-PL
 * @version v1.0
 */
public class ImageTweakerTool {

    private int userID, versionAvatar;

    public ImageTweakerTool(int userID){
        this.userID = userID;
    }

    public ImageTweakerTool(int userID, int versionAvatar){
        this.userID = userID;
        this.versionAvatar = versionAvatar;
    }

    public String transformImage(File file, boolean isUser) {
        if (file != null){
            try {
                BufferedImage originalImgage = ImageIO.read(file);
                BufferedImage subImage = null;

                if (originalImgage.getWidth() == originalImgage.getHeight()){
                    subImage = originalImgage;
                }else {
                    if (originalImgage.getWidth() > originalImgage.getHeight()){
                        int xPos = (originalImgage.getWidth() - originalImgage.getHeight()) / 2;
                        subImage = originalImgage.getSubimage(xPos, 0, originalImgage.getHeight(), originalImgage.getHeight());
                    }else {
                        int yPos = (originalImgage.getHeight() - originalImgage.getWidth()) / 2;
                        subImage = originalImgage.getSubimage(0, yPos, originalImgage.getWidth(), originalImgage.getWidth());
                    }
                }
                subImage = Scalr.resize(subImage, 256, 256);
                if (isUser)
                    return uploadImageUser(subImage, Integer.toString(userID));
                else{
                    return uploadImageGroup(subImage, Integer.toString(userID));

                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[ERROR] - Failed to transform the selected image...");
            }
        }else
            System.out.println("[ERROR] - The selected file is not valid or it's null...");

        return null;
    }

    public String uploadImageUser(BufferedImage subImage, String fileName){

        try {
            File outputfile = new File(fileName + ".jpg");
            ImageIO.write(subImage, "JPG", outputfile);
            Map mapUpload = Data.cloudAPI.uploader().upload(outputfile, ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", "profilePics/users/" + fileName
            ));
            System.out.println("[INFO] - Success! Uploaded to Cloudinary!");
            Data.userManager.updateAvatar(userID, (Integer) mapUpload.get("version"));
            return (String) mapUpload.get("url");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public String uploadImageGroup(BufferedImage subImage, String fileName){

        try {
            File outputfile = new File(fileName + ".jpg");
            ImageIO.write(subImage, "JPG", outputfile);
            Map mapUpload = Data.cloudAPI.uploader().upload(outputfile, ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", "profilePics/groups/" + fileName
            ));
            System.out.println("[INFO] - Success! Uploaded to Cloudinary!");
            Data.userManager.updateAvatar(userID, (Integer) mapUpload.get("version"));
            return (String) mapUpload.get("url");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File importImage(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("JPG, JPEG & PNG","jpg", "jpeg","png");
        fileChooser.setFileFilter(filtroImagen);
        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        }

        return null;
    }

    public String getProfilePicUser(){
        try {
            String imageURL;
            if (versionAvatar == 0){
                imageURL = Data.cloudAPI.url().imageTag("profilePics/users/"+userID+".jpg");
            }else{
                imageURL = Data.cloudAPI.url().version(versionAvatar).imageTag("profilePics/users/"+userID+".jpg");
            }
            System.out.println(imageURL);
            String[] urlPic = imageURL.split("'");
            Image image = new Image(urlPic[1]);
            if (!image.isError()) {
                return urlPic[1];
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "https://res.cloudinary.com/multitaskapp/image/upload/v1618085740/profilePics/default.jpg";
    }

    public String getProfilePicGroup(){
        try {
            int groupID = userID;
            String imageURL = Data.cloudAPI.url().imageTag("profilePics/groups/"+groupID+".jpg");
            String[] urlPic = imageURL.split("'");
            Image image = new Image(urlPic[1]);
            if (!image.isError()) {
                return urlPic[1];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "https://res.cloudinary.com/multitaskapp/image/upload/v1618085740/profilePics/default.jpg";
    }

}
