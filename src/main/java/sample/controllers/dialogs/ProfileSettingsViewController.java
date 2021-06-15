package sample.controllers.dialogs;

import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import sample.utils.Data;
import sample.utils.Encrypter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileSettingsViewController implements Initializable {

    @FXML    private PasswordField pfCurrent, pfNewPass1, pfNewPass2;
    @FXML    private Button btnChangePass, btnDeleteAccount, btnUpdatePrivacity;
    @FXML    private JFXToggleButton switchBirthday, switchAddress, switchLinks, switchTlf;

    private ArrayList<JFXToggleButton> arraySwitchs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setSwitchesStatus();

        arraySwitchs = new ArrayList<>(
                Arrays.asList(
                        switchBirthday, switchAddress, switchLinks, switchTlf
                )
        );

        for (JFXToggleButton  btnSwitch : arraySwitchs) {
            btnSwitch.setToggleColor(Paint.valueOf(Data.userData.getHexCode()));
            btnSwitch.setToggleLineColor(Paint.valueOf(Data.userData.getHexCode()));
        }

    }

    private void setSwitchesStatus() {
        JSONArray privacityJSON = Data.userData.getPrivacitySettings();
        if (privacityJSON != null){
            switchBirthday.setSelected(privacityJSON.getInt(0) == 1);
            switchAddress.setSelected(privacityJSON.getInt(1) == 1);
            switchLinks.setSelected(privacityJSON.getInt(2) == 1);
            switchTlf.setSelected(privacityJSON.getInt(3) == 1);
        }else{
            switchBirthday.setSelected(false);
            switchAddress.setSelected(false);
            switchLinks.setSelected(false);
            switchTlf.setSelected(false);
        }
    }

    public void disconnect(Stage thisStage) {
        try {
            Data.properties.clear();
            Data.storeProperties(Data.properties);
            System.out.println("[DEBUG] - Datos del usuario internos eliminados correctamente!");
            thisStage.close();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/login.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MultitaskAPP | DESKTOP");
            stage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image("windows/res/icons/multitask_icon.png");
            stage.getIcons().add(icon);
            stage.getScene().setFill(LinearGradient.valueOf("from 0% 0% to 100% 0%, #121214 0%,  #121214 21%, #202027 22%, #202027 100%"));

            Data.mainStage = stage;

            stage.show();

        }catch (Exception e){
            System.out.println("[DEBUG] - Error al cerrar la sesion...");
        }
    }

    @FXML
    void checkDeleteAccount(MouseEvent event) {
        Alert verifyAlert = new Alert(Alert.AlertType.WARNING);
        verifyAlert.setTitle("MultitaskAPP");
        verifyAlert.setHeaderText("Estas seguro de que quieres eliminar tu cuenta? No hay vuelta atrás...");
        verifyAlert.getButtonTypes().add(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));

        Optional<ButtonType> result = verifyAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean success = Data.userManager.deleteAccount();
            if (success){
                disconnect((Stage) btnChangePass.getScene().getWindow());
            }
        }
    }

    @FXML
    void checkPassword(MouseEvent event) {
        if (Data.userData.getPass().equals(Encrypter.hashMD5(pfCurrent.getText()))){
            if (!(pfNewPass1.getText().isBlank()) || !(pfNewPass2.getText().isBlank())){
                if (pfNewPass1.getText().equals(pfNewPass2.getText())){
                    boolean success = Data.userManager.changePassword(Encrypter.hashMD5(pfNewPass1.getText()));
                    if (success){
                        pfCurrent.clear();
                        pfNewPass1.clear();
                        pfNewPass2.clear();
                        Data.userData.setPass(Encrypter.hashMD5(pfNewPass1.getText()));
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("MultitaskAPP");
                        alert.setHeaderText("Contrseña actualizada correctamente!");
                        alert.showAndWait();
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("MultitaskAPP");
                        alert.setHeaderText("Ha ocurrido un error, no se ha cambiado la contraseña");
                        alert.showAndWait();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("MultitaskAPP");
                    alert.setHeaderText("La nueva contraseña no coincide....");
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Una de las dos contraseñas esta en blanco o no es valida...");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("La contraseña que has introducido no es correcta...");
            alert.showAndWait();
        }
    }

    @FXML
    void checkPrivacity(MouseEvent event) {
        JSONArray privacityJSON = new JSONArray();
        for (JFXToggleButton btnSwitch : arraySwitchs) {
            if (btnSwitch.isSelected())
                privacityJSON.put(1);
            else privacityJSON.put(0);
        }

        boolean success = Data.userManager.updatePrivacity(privacityJSON);
        if (success){
            Data.userData.setPrivacitySettings(privacityJSON);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Privacidad actualizada correctamente!");
            alert.showAndWait();
        }

    }
}
