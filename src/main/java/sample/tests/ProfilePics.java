package sample.tests;

import org.imgscalr.Scalr;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ProfilePics extends Thread{

    public static void main(String[] args) {
        try {
            // BufferedImage originalImgage = ImageIO.read(new File("C:\\Users\\xavip\\Desktop\\misina1.jpeg"));
            File file = new File("C:\\Users\\xavip\\Downloads\\mvk.jpg");
            String fileName = file.getName();

            BufferedImage originalImgage = ImageIO.read(file);
            System.out.println("Original Image Dimension: "+originalImgage.getWidth()+"x"+originalImgage.getHeight());

            BufferedImage SubImage = null;
            if (originalImgage.getWidth() == originalImgage.getHeight()){
                SubImage = originalImgage;
            }else {
                if (originalImgage.getWidth() > originalImgage.getHeight()){

                    int xPos = (originalImgage.getWidth() - originalImgage.getHeight()) / 2;
                    SubImage = originalImgage.getSubimage(xPos, 0, originalImgage.getHeight(), originalImgage.getHeight());
                }else {
                    int yPos = (originalImgage.getHeight() - originalImgage.getWidth()) / 2;
                    SubImage = originalImgage.getSubimage(0, yPos, originalImgage.getWidth(), originalImgage.getWidth());
                }
            }

            SubImage = Scalr.resize(SubImage, 256, 256);

            System.out.println("Cropped Image Dimension: "+SubImage.getWidth()+"x"+SubImage.getHeight());
            File outputfile = new File("C:/Test/croppedImage.jpg");
            ImageIO.write(SubImage, "jpg", outputfile);






            /*Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "multitaskapp",
                    "api_key", "169861562753297",
                    "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
            ));

            cloudAPI.uploader().upload(outputfile, ObjectUtils.asMap(
                    "resource_type", "image",
                    "public_id", "profilePics/" + fileName
            ));*/

            System.out.println("Image cropped successfully: "+outputfile.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}