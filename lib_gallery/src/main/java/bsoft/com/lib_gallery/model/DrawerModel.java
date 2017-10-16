package bsoft.com.lib_gallery.model;

public class DrawerModel {
    public String iconPath = null;
    public int total = 0;
    private int icon = 0;
    private String title = null;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}