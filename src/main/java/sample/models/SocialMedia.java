package sample.models;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;

public class SocialMedia {

    private String name;
    private String url;
    private FontAwesomeIcon icon;
    private String hexCode;

    public SocialMedia() {}

    public SocialMedia(String name, String url, String icon, String hexCode){
        this.name = name;
        this.url = url;
        this.icon = new FontAwesomeIcon();
        this.icon.setIcon(FontAwesomeIconName.valueOf(icon));
        this.hexCode = hexCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FontAwesomeIcon getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = new FontAwesomeIcon();
        this.icon.setIcon(FontAwesomeIconName.valueOf(icon));
    }

    public void setIcon(FontAwesomeIcon icon) {
        this.icon = icon;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }
}
