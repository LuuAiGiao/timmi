package cf20.soft.shoes.timmi.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cf20.soft.shoes.timmi.admin.CommonState;
import cf20.soft.shoes.timmi.model.Order;

public class DetailViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Boolean> addCartFlow;

    public DetailViewModel() {
        addCartFlow = new MutableLiveData<>();
    }

    public LiveData<Boolean> getAdCartFlow() {
        return addCartFlow;
    }

    public void addCart(Order order) {
        Map<String, Object> cart = new HashMap<>();
        cart.put("userId", CommonState.getUser().getId());
        cart.put("productId", order.getProduct().getId());
        cart.put("color", order.getColor());
        cart.put("size", order.getSize());

        db.collection("carts").add(cart).addOnSuccessListener(documentReference -> {
            addCartFlow.postValue(true);
        }).addOnFailureListener(e -> {
            addCartFlow.postValue(false);
        });
    }
}
