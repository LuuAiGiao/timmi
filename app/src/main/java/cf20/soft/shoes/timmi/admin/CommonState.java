package cf20.soft.shoes.timmi.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import cf20.soft.shoes.timmi.model.Order;
import cf20.soft.shoes.timmi.model.PreOrder;
import cf20.soft.shoes.timmi.model.Product;
import cf20.soft.shoes.timmi.model.User;

public class CommonState {

    private static final MutableLiveData<Boolean> refreshFlow = new MutableLiveData<>();
    private static PreOrder preOrder = new PreOrder();

    public static PreOrder getPreOrder() {
        return preOrder;
    }

    public static void postValueRefresh(boolean refresh) {
        refreshFlow.postValue(refresh);
    }

    public static LiveData<Boolean> getRefreshFlow() {
        return refreshFlow;
    }

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        CommonState.user = user;
        if (user != null) {
            preOrder.setUserId(user.getId());
        }
    }

    public static void addCart(Order order) {
//        ArrayList<Order> orders = new ArrayList<>(preOrder.getOrders());
//        orders.add(order);
        preOrder.getOrders().add(order);
    }

    private static List<Product> products;

    public static void setCacheProducts(List<Product> temp) {
        products = temp;
    }

    public static List<Product> getCacheProducts() {
        if (products == null) return new ArrayList<>();
        return products;
    }
}
