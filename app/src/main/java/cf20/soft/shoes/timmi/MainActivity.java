package cf20.soft.shoes.timmi;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import cf20.soft.shoes.timmi.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public NavController getNavController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}