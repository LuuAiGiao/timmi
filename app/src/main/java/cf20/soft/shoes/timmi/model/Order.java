package cf20.soft.shoes.timmi.model;

public class Order {
    private String id;

    private Product product;
    private String color;
    private String size;

    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Order(Product product, String id, String color, String size) {
        this.product = product;
        this.id = id;
        this.color = color;
        this.size = size;
    }

    public Order(String id, String color, String size) {
        this.id = id;
        this.color = color;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
