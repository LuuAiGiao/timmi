package cf20.soft.shoes.timmi.ui.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cf20.soft.shoes.timmi.model.Product;
import cf20.soft.shoes.timmi.model.User;

public class RegisterViewModel extends ViewModel {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Boolean> userExistsFlow;
    private final MutableLiveData<User> createFlow;

    public RegisterViewModel() {
        userExistsFlow = new MutableLiveData<>();
        createFlow = new MutableLiveData<>();
    }

    public LiveData<Boolean> getUserExistsFlow() {
        return userExistsFlow;
    }

    public LiveData<User> getCreateFlow() {
        return createFlow;
    }

    public void register(String email, String name, String pass) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().isEmpty()) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("pass", pass);
                        user.put("name", name);

                        db.collection("users").add(user)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("createProduct", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    createFlow.postValue(new User(email, name, ""));
                                }).addOnFailureListener(e -> {
                                    Log.w("createProduct", "Error adding document", e);
                                    createFlow.postValue(null);
                                });
                    } else {
                        userExistsFlow.postValue(true);
                    }
                })
                .addOnFailureListener(e -> {
                    createFlow.postValue(null);
                });
    }
}
