package sample.models;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;

public class SocialMedia {

    private String name;
    private String url;
    private FontAwesomeIcon icon;

    public SocialMedia() {}

    public SocialMedia(String name, String url, String icon){
        this.name = name;
        this.url = url;
        this.icon = new FontAwesomeIcon();
        this.icon.setIcon(FontAwesomeIconName.valueOf(icon));
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

    @Override
    public String toString() {
        return name;
    }
}
