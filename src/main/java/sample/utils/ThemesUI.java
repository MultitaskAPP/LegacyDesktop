package sample.utils;

import sample.models.Theme;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ThemesUI {

    public static Theme selectedTheme;
    public static ArrayList<Theme> themeList = new ArrayList<Theme>(
            Arrays.asList(
                    new Theme("DarkTheme", Color.decode("#121214"), Color.decode("#202027"), Color.decode("#272730"), Color.decode("#32323E"), Color.decode("#FFFFFF")),
                    new Theme("LightTheme", Color.decode("#E3E5E8"), Color.decode("#F2F3F5"), Color.decode("#FFFFFF"), Color.decode("#E3E5E8"), Color.decode("#000000"))            )
    );

    public void changeTheme(Theme selectedTheme){
        this.selectedTheme = selectedTheme;
        Data.properties.setProperty("themeUI",selectedTheme.getThemeName());
        Data.storeProperties(Data.properties);
    }

}
