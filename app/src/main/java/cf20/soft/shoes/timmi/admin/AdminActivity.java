package cf20.soft.shoes.timmi.admin;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import cf20.soft.shoes.timmi.R;
import cf20.soft.shoes.timmi.databinding.ActivityAdminBinding;
import cf20.soft.shoes.timmi.databinding.ActivityMainBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        NavigationUI.setupWithNavController(binding.navView, getNavController());
    }

    public NavController getNavController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showNavBottom();
    }

    public void hideNavBottom() {
        binding.navView.setVisibility(View.GONE);
    }

    public void showNavBottom() {
        binding.navView.setVisibility(View.VISIBLE);
    }
}