package com.dangchuvmnewb;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.dangchuvmnewb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private long extime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.textView.setText("Chào, " + getString(R.string.app_name) + " Activity!");
        binding.textView.setOnClickListener(v -> {
            Toast.makeText(this, getString(R.string.app_name),Toast.LENGTH_SHORT).show(); // Lấy app name khi click vào textView
        });
    }

    @Override
    public void onBackPressed() {
        if(extime + 2000 > System.currentTimeMillis()) { //Mặc định sẽ là 2 giây
        	finish();
        } else {
        	Toast.makeText(this, "Nhấp lần nữa để thoát.",Toast.LENGTH_SHORT).show();
        }
        extime = System.currentTimeMillis();
    }
}
