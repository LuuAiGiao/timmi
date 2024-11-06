package cf20.soft.shoes.timmi.ui.cart;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

import java.util.HashMap;
import java.util.Map;

import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.model.Order;
import cf20.soft.shoes.timmi.model.PreOrder;
import cf20.soft.shoes.timmi.model.Product;
import cf20.soft.shoes.timmi.model.User;

public class CartViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<PreOrder> preOrderFlow;
    private final MutableLiveData<Boolean> deleteFlow;
    private final MutableLiveData<Boolean> createFlow;

    public LiveData<Boolean> getDeleteFlow() {
        return deleteFlow;
    }
    public LiveData<Boolean> getCreateFlow() {
        return createFlow;
    }

    public LiveData<PreOrder> getPreOrderFlow() {
        return preOrderFlow;
    }

    public CartViewModel() {
        preOrderFlow = new MutableLiveData<>();
        deleteFlow = new MutableLiveData<>();
        createFlow = new MutableLiveData<>();
        getCart();
    }

    public void getCart() {
        db.collection("carts")
                .whereEqualTo("userId", CommonState.getUser().getId())
                .get()
                .addOnCompleteListener(task -> {
                    PreOrder preOrder = new PreOrder();
                    if (!task.getResult().isEmpty()) {
                        preOrder.setUserId(CommonState.getPreOrder().getUserId());
                        ArrayList<Order> listOrder = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();

                            Order order = new Order(document.getId(), (String) data.get("color"), (String) data.get("size"));
                            listOrder.add(order);

                            for (Product product : CommonState.getCacheProducts()) {
                                if (product.getId().equals(data.get("productId"))) {
                                    order.setProduct(product);
                                    break;
                                }
                            }
                        }
                        preOrder.setOrders(listOrder);
                    }
                    preOrderFlow.postValue(preOrder);
                })
                .addOnFailureListener(e -> {
                    preOrderFlow.postValue(null);
                });
    }

    public void addBill(PreOrder preOrder, String address, String phone, long total) {

        Map<String, Object> bill = new HashMap<>();
        bill.put("userId", preOrder.getUserId());

        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        for (Order order : preOrder.getOrders()) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", order.getProduct().getId());
            product.put("color", order.getColor());
            product.put("size", order.getSize());

            maps.add(product);
        }
        bill.put("product", maps);
        bill.put("address", address);
        bill.put("phone", phone);
        bill.put("total", total);

        db.collection("bills").add(bill)
                .addOnSuccessListener(documentReference -> {
                    for (Order order : preOrder.getOrders()) {
                        db.collection("carts").document(order.getId()).delete();
                    }
                    createFlow.postValue(true);
                }).addOnFailureListener(e -> {
                    createFlow.postValue(false);
                });
    }

    public void deleteOrder(Order order) {
        db.collection("carts").document(order.getId())
                .delete()
                .addOnSuccessListener(aVoid -> deleteFlow.postValue(true))
                .addOnFailureListener(e -> deleteFlow.postValue(false));
    }

    public void clearDeleteFlow() {
        deleteFlow.postValue(null);
    }
}
