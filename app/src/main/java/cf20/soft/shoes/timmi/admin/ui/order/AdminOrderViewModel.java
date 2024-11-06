package cf20.soft.shoes.timmi.admin.ui.order;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cf20.soft.shoes.timmi.model.Bill;
import cf20.soft.shoes.timmi.model.Order;

public class AdminOrderViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<Bill>> billsFlow;

    public LiveData<List<Bill>> getBillsFlow() {
        return billsFlow;
    }

    public AdminOrderViewModel() {
        billsFlow = new MutableLiveData<>();
        getBill();
    }

    public void getBill() {
        db.collection("bills")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Bill> bills = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();

                                ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) data.get("product");

                                ArrayList<Order> orders = new ArrayList<>();
                                for (Map<String, Object> item : list) {
                                    Order order = new Order();
                                    order.setColor(item.get("color").toString());
                                    order.setSize(item.get("size").toString());
                                    order.setProductId(item.get("productId").toString());

                                    orders.add(order);
                                }
                                Bill bill = new Bill(document.getId(), (String) data.get("address"), (String) data.get("phone"), orders, (String) data.get("userId"), (Long) data.get("total"));
                                bills.add(bill);
                            }
                            billsFlow.postValue(bills);
                        } else {
                            Log.w("AdminProductViewModel", "Error getting documents.", task.getException());
                            billsFlow.postValue(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        billsFlow.postValue(null);
                    }
                });
    }
}
