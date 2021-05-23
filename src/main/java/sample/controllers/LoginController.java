package sample.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.Encrypter;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public static double x, y;

    @FXML    private AnchorPane loginPane, registerPane;
    @FXML    private Button btnLogin, btnRegister;
    @FXML    private TextField tfEmail, rTfName, rTfSurname1, rTfSurname2, rTfEmail;
    @FXML    private PasswordField pfPassword, rPfPassword1, rPfPassword2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gotoLogin(null);
        pfPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER))
                    checkLogin(null);
            }
        });

    }

    @FXML
    void windowDrag(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void gotoLogin(MouseEvent  event) {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    @FXML
    void gotoRegister(MouseEvent  event) {
        registerPane.setVisible(true);
        loginPane.setVisible(false);
    }

    @FXML
    void checkLogin(ActionEvent event) {

        // Comprobar si los datos introducidos en el login son válidos antes de llamar a la API

        boolean validated = false;
        if (tfEmail.getText().contains("@"))
            validated = true;

        if (validated){
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("email", tfEmail.getText());
            requestJSON.put("pass", Encrypter.hashMD5(pfPassword.getText()));

            ConnAPI connAPI = new ConnAPI("/api/login", "POST", false);
            connAPI.setData(requestJSON);
            connAPI.establishConn();

            JSONObject responseJSON = connAPI.getDataJSON();
            int status = connAPI.getStatus();
            switch (status){
                case 0:
                    System.out.println("[DEBUG] - Error al contactar con la API");
                    break;

                case 200:
                    JSONArray arrayJSON = new JSONArray(responseJSON.getJSONArray("data"));
                    Data.userData = Data.userManager.getUserData(arrayJSON.getJSONObject(0));
                    Data.arrayGroupsUser = Data.groupManager.getAllGroups(Data.userData.getIdUser());
                    storeLoginStatus();
                    gotoMainWindow();
                    break;

                case 500:
                    String errorMSG = responseJSON.getString("message");
                    System.out.println("[ERROR] - " + errorMSG);
                    pfPassword.clear();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("MultitaskAPP | DESKTOP");
                    alert.setHeaderText("Login incorrecto...");
                    alert.showAndWait();
                    break;
            }
        }else {
            System.out.println("[DEBUG] - Datos no válidos...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Datos invalidos...");
            alert.showAndWait();
        }
    }

    @FXML
    void checkRegister(ActionEvent event) {

        // Comprobar si los datos introducidos són validos

        boolean validated = false;
        if (!rTfName.getText().isBlank()){
            if (rTfEmail.getText().contains("@")){
                if (rPfPassword1.getText().equals(rPfPassword2.getText())){
                    validated = true;
                }else System.out.println("[DEBUG] - Las contraseñas no coinciden...");
            }else System.out.println("[DEBUG] - El correo electronico introducido esta en blanco o no es válido...");
        }else System.out.println("[DEBUG] - El nombre introducido esta en blanco o no es válido...");

        if (validated){

            // Se monta el JSON con los datos introducios en el formulario de registro.

            JSONObject registerData = new JSONObject();
            registerData.put("name", rTfName.getText());
            registerData.put("email", rTfEmail.getText());
            registerData.put("password", Encrypter.hashMD5(rPfPassword1.getText()));
            if (!rTfSurname1.getText().isBlank())
                registerData.put("firstSurname", rTfSurname1.getText());
            if (!rTfSurname2.getText().isBlank())
                registerData.put("lastSurname", rTfSurname2.getText());

            // Llamada API para registrar el usuario

            JSONObject requestJSON = new JSONObject();
            requestJSON.put("data", registerData);

            ConnAPI connAPI = new ConnAPI("/api/register", "POST", false);
            connAPI.setData(requestJSON);
            connAPI.establishConn();

            JSONObject responseJSON = connAPI.getDataJSON();
            int status = connAPI.getStatus();
            switch (status){
                case 0:
                    System.out.println("[DEBUG] - Error al contactar con la API");
                    break;

                case 200:
                    System.out.println("[DEBUG] - Usuario registrado correctamente!");
                    JSONObject result = responseJSON.getJSONObject("data");
                    Data.userData = Data.userManager.getUserData(result);
                    gotoMainWindow();
                    break;

                case 500:
                    String errorMSG = responseJSON.getString("message");
                    System.out.println("[ERROR] - " + errorMSG);
                    rPfPassword1.clear();
                    rPfPassword2.clear();
                    break;
            }

        }
    }

    private void gotoMainWindow(){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/mainWindow.fxml").toURI().toURL();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/mainWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MultitaskAPP | DESKTOP");
            stage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image("windows/res/multitask_icon.png");
            stage.getIcons().add(icon);
            stage.show();

            Stage thisStage = (Stage) btnRegister.getScene().getWindow();
            thisStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeLoginStatus(){
        Algorithm algorithm = Algorithm.HMAC256("b568ef297c5755b85e7a0df8783ce04b");
        String token = JWT.create().withSubject(Integer.toString(Data.userData.getIdUser())).sign(algorithm);
        Data.properties.setProperty("tokenLogin", token);
        Data.storeProperties(Data.properties);
    }

    @FXML
    void recoverPassword(MouseEvent event) {

    }

}
