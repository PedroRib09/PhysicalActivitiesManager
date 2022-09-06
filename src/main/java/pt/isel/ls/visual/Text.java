package pt.isel.ls.visual;

public class Text {

    private String titleText;
    Object parent;


    Text(String titleText) {
        this.titleText = titleText;
    }

    @Override
    public String toString() {
        return titleText;
    }
}