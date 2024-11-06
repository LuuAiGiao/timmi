package cf20.soft.shoes.timmi.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

import cf20.soft.shoes.timmi.model.User;

public class LoginViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<User> loginFlow;

    public LoginViewModel() {
        loginFlow = new MutableLiveData<>();
    }

    public LiveData<User> getLoginFlow() {
        return loginFlow;
    }

    public void login(String email, String password) {
        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("pass", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().isEmpty()) {
                        loginFlow.postValue(null);
                    } else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            User user = new User(
                                    document.getId(),
                                    (String) data.get("name"),
                                    ""
                            );
                            loginFlow.postValue(user);
                            break;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    loginFlow.postValue(null);
                });
    }
}
