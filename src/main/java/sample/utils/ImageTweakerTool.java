package sample.utils;

import com.cloudinary.utils.ObjectUtils;
import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

    private File profilePicFile;
    private String userID;

    public static void main(String[] args) {
        ImageTweakerTool imageTweakerTool = new ImageTweakerTool("39847598034659");
        File file = imageTweakerTool.importImage();
    }

    public ImageTweakerTool(String userID){
        this.userID = userID;
    }

    /**
     * Centers the selected image and transforms it to 256x256px & 1:1 ratio.
     * @param file The selected file from the JFileChooser
     */
    public void transformImage(File file, String userID) {
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
                uploadImage(subImage, userID);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("[ERROR] - Failed to transform the selected image...");
            }
        }else
            System.out.println("[ERROR] - The selected file is not valid or it's null...");

    }

    /**
     * Converts the image to file and uploads image to Cloudinary via API
     * @param subImage Image to upload to Cloudinary
     * @param fileName Id of the user
     */
    public void uploadImage(BufferedImage subImage, String fileName){
        try {
            File outputfile = new File(fileName + ".jpg");
            ImageIO.write(subImage, "JPG", outputfile);
            Data.cloudAPI.uploader().upload(outputfile, ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", "profilePics/" + fileName
            ));
            System.out.println("[INFO] - Success! Uploaded to Cloudinary!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforms and exports the image to the output folder
     * EX: "output/itt_imageName.ext"
     * @param subImage Image to export to file
     * @param fileName Id of the user
     */
    public void exportImage(BufferedImage subImage, String fileName) {
        try {
            File outputFolder = new File("output");
            if (!outputFolder.exists())
                outputFolder.mkdir();
            File outputfile = new File(outputFolder.getAbsolutePath() + "/itt_" + fileName);
            ImageIO.write(subImage, FilenameUtils.getExtension(outputfile.getAbsolutePath()), outputfile);
            System.out.println("[INFO] - Success! Exported to: ["+outputfile.getAbsolutePath() + "]");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[ERROR] - Failed to export the selected image...");
        }
    }

    /**
     * Instances a new JFileChooser and the users has to select the image to edit
     * @serialData JPEG, JPG and PNG extensions.
     * @return The selected File, if error returns null and the program stops.
     */
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

    /**
     * In case of error returns a defualt profilePic
     * @return The URL of the profilePic stored on Cloudinary
     */
    public String getProfilePic(){
        try {
            String imageURL = Data.cloudAPI.url().imageTag("profilePics/"+userID+".jpg");
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
