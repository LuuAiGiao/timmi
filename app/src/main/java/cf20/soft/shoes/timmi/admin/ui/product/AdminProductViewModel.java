package cf20.soft.shoes.timmi.admin.ui.product;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cf20.soft.shoes.timmi.model.Product;

public class AdminProductViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<Product>> productsFlow;
    private final MutableLiveData<Boolean> deleteFlow;

    public LiveData<List<Product>> getProductsFlow() {
        return productsFlow;
    }

    public LiveData<Boolean> getDeleteFlow() {
        return deleteFlow;
    }

    public AdminProductViewModel() {
        productsFlow = new MutableLiveData<>();
        deleteFlow = new MutableLiveData<>();
        getData();
    }

    public void getData() {
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<Product> products = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("AdminProductViewModel", document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                Product product = new Product(
                                        document.getId(),
                                        (String) data.get("name"),
                                        (List<String>) data.get("image"),
                                        (List<String>) data.get("colors"),
                                        (List<String>) data.get("sizes"),
                                        (Long) data.get("price"),
                                        (Long) data.get("sale"),
                                        (Long) data.get("quality")
                                );
                                products.add(product);
                            }
                        } else {
                            Log.w("AdminProductViewModel", "Error getting documents.", task.getException());
                        }

                        productsFlow.postValue(products);
                    }
                });
    }

    public void deleteItem(Product product) {
        db.collection("products").document(product.getId())
                .delete()
                .addOnSuccessListener(aVoid -> deleteFlow.postValue(true))
                .addOnFailureListener(e -> deleteFlow.postValue(false));
    }

    public void clearDeleteFlow() {
        deleteFlow.postValue(null);
    }
}
