package cf20.soft.shoes.timmi.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Product implements Serializable {

    private String id;
    private String name;
    private List<String> image;
    private List<String> colors;
    private List<String> sizes;
    private long price;
    private long sale;
    private long quality;

    public Product() {
    }

    public Product(String id, String name, List<String> image, List<String> colors, List<String> sizes, long price, long sale, long quality) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.colors = colors;
        this.sizes = sizes;
        this.price = price;
        this.sale = sale;
        this.quality = quality;
    }

    public Product(String name, List<String> image, List<String> colors, List<String> sizes, long price, long sale, long quality) {
        this.name = name;
        this.image = image;
        this.colors = colors;
        this.sizes = sizes;
        this.price = price;
        this.sale = sale;
        this.quality = quality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }


    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<String> getColors() {
        return colors;
    }

    public String getColorsV2() {
        if (colors.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        for (String s : colors) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(s);
        }
        return builder.toString().trim();
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public String getSizesV2() {
        if (sizes.isEmpty()) return "";
        StringBuilder builder = new StringBuilder();
        for (String s : sizes) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(s);
        }
        return builder.toString().trim();
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSale() {
        return sale;
    }

    public void setSale(long sale) {
        this.sale = sale;
    }

    public long getQuality() {
        return quality;
    }

    public void setQuality(long quality) {
        this.quality = quality;
    }
}
