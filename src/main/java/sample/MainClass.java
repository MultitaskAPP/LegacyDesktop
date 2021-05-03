package sample;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.models.User;
import sample.utils.ConnAPI;
import sample.utils.Data;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        getProperties();

        URL url;
        if (Data.properties.getProperty("tokenLogin") == null){
            url = new File("src/main/java/sample/windows/login.fxml").toURI().toURL();
        }else{
            Data.userData = decodeToken(Data.properties.getProperty("tokenLogin"));
            Data.arrayGroupsUser = Data.groupManager.getAllGroups(Data.userData.getIdUser());
            url = new File("src/main/java/sample/windows/mainWindow.fxml").toURI().toURL();

        }

        Parent root = FXMLLoader.load(url);
        root.setEffect(new DropShadow());
        primaryStage.setTitle("MultitaskAPP | DESKTOP");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1280, 720));

        url = new File("src/main/java/sample/windows/res/multitask_icon.png").toURI().toURL();
        Image icon = new Image(String.valueOf(url));
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }

    private User decodeToken(String tokenLogin) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("b568ef297c5755b85e7a0df8783ce04b");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(tokenLogin);

            JSONObject requestJSON = new JSONObject();
            requestJSON.put("id", jwt.getSubject());

            ConnAPI connAPI = new ConnAPI("/api/login/token", "POST", false);
            connAPI.setData(requestJSON);
            connAPI.establishConn();

            JSONObject responseJSON = connAPI.getDataJSON();
            int status = connAPI.getStatus();
            if (status == 200){
                JSONArray arrayJSON = new JSONArray(responseJSON.getJSONArray("data"));
                return Data.userManager.getUserData(arrayJSON.getJSONObject(0));
            }
        }catch (Exception e ){
            e.printStackTrace();
        }


        return null;

    }

    private void getProperties(){

        try(InputStream inputStream = new FileInputStream("config.properties")){
            Data.properties.load(inputStream);
            System.out.println("[DEBUG] - Propiedades cargadas correctamente!");
        } catch (FileNotFoundException e) {
            System.out.println("[DEBUG] - Fichero .properties no encontrado, creando uno nuevo...");
            setProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setProperties(){
        Data.storeProperties(Data.properties);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
