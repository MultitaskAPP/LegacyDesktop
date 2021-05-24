package sample.models;

import java.awt.*;

public class Theme {

    private String themeName;
    private Color backgroundColour;
    private Color primaryColour;
    private Color secondaryColour;
    private Color tertiaryColour;
    private Color textColour;

    public Theme() {}

    public Theme(String themeName, Color backgroundColour, Color primaryColour, Color secondaryColour, Color tertiaryColour, Color textColour) {
        this.themeName = themeName;
        this.backgroundColour = backgroundColour;
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
        this.tertiaryColour = tertiaryColour;
        this.textColour = textColour;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public Color getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(Color primaryColour) {
        this.primaryColour = primaryColour;
    }

    public Color getSecondaryColour() {
        return secondaryColour;
    }

    public void setSecondaryColour(Color secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public Color getTertiaryColour() {
        return tertiaryColour;
    }

    public void setTertiaryColour(Color tertiaryColour) {
        this.tertiaryColour = tertiaryColour;
    }

    public Color getTextColour() {
        return textColour;
    }

    public void setTextColour(Color textColour) {
        this.textColour = textColour;
    }

    public String getHexColour(Color c){

        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    @Override
    public String toString() {
        return themeName;
    }
}
