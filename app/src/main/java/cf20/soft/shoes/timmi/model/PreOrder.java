package cf20.soft.shoes.timmi.model;

import java.util.ArrayList;
import java.util.List;

public class PreOrder {
    private String userId;
    private List<Order> orders;

    public PreOrder(List<Order> orders, String userId) {
        this.orders = orders;
        this.userId = userId;
    }

    public PreOrder() {
        orders = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}