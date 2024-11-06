package cf20.soft.shoes.timmi.model;

public class ColorOrSize {

    private String value;
    private boolean isSelected;

    public ColorOrSize(String value, boolean isSelected) {
        this.value = value;
        this.isSelected = isSelected;
    }

    public ColorOrSize() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
