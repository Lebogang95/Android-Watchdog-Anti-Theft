package za.co.lbnkosi.watchdog.utils;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class MainMenuMap {
    private String title;
    private String description;
    private String iconLink;

    public MainMenuMap() {
    }

    public MainMenuMap(String title, String description, String iconLink) {
        this.title = title;
        this.description = description;
        this.iconLink = iconLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    //

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description= description;
    }

    public String getIconLink(){ return iconLink; }

    public void setIconLink(String iconLink){ this.iconLink = iconLink;}
}
