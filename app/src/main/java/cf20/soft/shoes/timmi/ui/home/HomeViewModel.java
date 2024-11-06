package cf20.soft.shoes.timmi.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cf20.soft.shoes.timmi.model.Product;

public class HomeViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<List<Product>> productsFlow;

    public LiveData<List<Product>> getProductsFlow() {
        return productsFlow;
    }

    public HomeViewModel() {
        productsFlow = new MutableLiveData<>();
        getData();
    }

    public void getData() {
        db.collection("products")
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<Product> products = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
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
                    }

                    productsFlow.postValue(products);
                });
    }
}