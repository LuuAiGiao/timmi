package cf20.soft.shoes.timmi.admin.ui.create;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import cf20.soft.shoes.timmi.model.Product;

public class CreateProductViewModel extends ViewModel {

    private final MutableLiveData<Boolean> createFlow;
    private final MutableLiveData<Boolean> updateFlow;

    public CreateProductViewModel() {
        createFlow = new MutableLiveData<>();
        updateFlow = new MutableLiveData<>();
    }

    public LiveData<Boolean> getCreateFlow() {
        return createFlow;
    }

    public LiveData<Boolean> getUpdateFlow() {
        return updateFlow;
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void createProduct(Product product) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", product.getName());
        user.put("image", product.getImage());
        user.put("colors", product.getColors());
        user.put("sizes", product.getSizes());
        user.put("price", product.getPrice());
        user.put("sale", product.getSale());
        user.put("quality", product.getQuality());

        // Add a new document with a generated ID
        db.collection("products").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("createProduct", "DocumentSnapshot added with ID: " + documentReference.getId());
                createFlow.postValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("createProduct", "Error adding document", e);
                createFlow.postValue(false);
            }
        });
    }

    public void editProduct(Product product) {
        // Create a new user with a first and last name
        Map<String, Object> inMap = new HashMap<>();
        inMap.put("name", product.getName());
        inMap.put("image", product.getImage());
        inMap.put("colors", product.getColors());
        inMap.put("sizes", product.getSizes());
        inMap.put("price", product.getPrice());
        inMap.put("sale", product.getSale());
        inMap.put("quality", product.getQuality());

        // Add a new document with a generated ID
        db.collection("products").document(product.getId()).set(inMap)
                .addOnSuccessListener(aVoid -> updateFlow.postValue(true))
                .addOnFailureListener(e -> updateFlow.postValue(false));
    }
}
