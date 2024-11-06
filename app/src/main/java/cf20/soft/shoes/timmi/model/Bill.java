package cf20.soft.shoes.timmi.model;

import java.util.List;

public class Bill {
    private String id;
    private String address;
    private String phone;
    private List<Order> product;
    private String userId;
    private long total;

    public Bill(String id, String address, String phone, List<Order> product, String userId, long total) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.product = product;
        this.userId = userId;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    private String userFullName;

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Bill() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Order> getProduct() {
        return product;
    }

    public void setProduct(List<Order> product) {
        this.product = product;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
