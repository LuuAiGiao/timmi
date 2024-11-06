package cf20.soft.shoes.timmi.admin.ui.edit;

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

public class EditProductViewModel extends ViewModel {

    private final MutableLiveData<Boolean> createFlow;

    public EditProductViewModel() {
        createFlow = new MutableLiveData<>();
    }

    public LiveData<Boolean> getCreateFlow() {
        return createFlow;
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
        db.collection("products")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("createProduct", "DocumentSnapshot added with ID: " + documentReference.getId());
                        createFlow.postValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("createProduct", "Error adding document", e);
                        createFlow.postValue(false);
                    }
                });
    }
}
