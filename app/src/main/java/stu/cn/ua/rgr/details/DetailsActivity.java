package stu.cn.ua.rgr.details;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

import stu.cn.ua.rgr.App;
import stu.cn.ua.rgr.R;
import stu.cn.ua.rgr.model.Meteorite;
import  androidx.fragment.app.FragmentTransaction;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String EXTRA_REPOSITORY_ID = "REPOSITORY_ID";

    private TextView nameTextView;
    private TextView fallTextView;
    private TextView yearTextView;
    private DetailsViewModel viewModel;
    private ProgressBar progressBar;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        nameTextView = findViewById(R.id.nameTextView);
        yearTextView = findViewById(R.id.yearTextView);
        fallTextView = findViewById(R.id.fallTextView);
        progressBar = findViewById(R.id.progress);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Meteorite details");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        long meteoriteId = getIntent().getLongExtra(EXTRA_REPOSITORY_ID, 1);
        if (meteoriteId == -1) {
            throw new RuntimeException("There is no meteorite ID");
        }

        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, app.getViewModelFactory());
        viewModel = viewModelProvider.get(DetailsViewModel.class);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        viewModel.loadMeteoriteById(meteoriteId);
        viewModel.getResults().observe(this, result -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (result.getStatus()) {
                case SUCCESS:
                    Meteorite meteorite = result.getData();
                    nameTextView.setText("Name: " + meteorite.getName());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    yearTextView.setText("Date: " + sdf.format(meteorite.getYear()));
                    fallTextView.setText("Fall: " + meteorite.getFall());
                    LatLng location = new LatLng(Double.parseDouble(meteorite.getGeolocation().getLatitude()),
                            Double.parseDouble(meteorite.getGeolocation().getLongitude()));
                    googleMap2.addMarker(new MarkerOptions().position(location).title(meteorite.getName() + " fall place"));
                    googleMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(location, -100f));
                    transaction.show(mapFragment);
                    transaction.commit();
                    progressBar.setVisibility(View.GONE);
                    break;
                case EMPTY:
                    nameTextView.setText("");
                    yearTextView.setText("");
                    fallTextView.setText("");
                    transaction.hide(mapFragment);
                    transaction.commit();
                    progressBar.setVisibility(View.GONE);
                    break;
                case LOADING:
                    nameTextView.setText("");
                    yearTextView.setText("");
                    fallTextView.setText("");
                    transaction.hide(mapFragment);
                    transaction.commit();
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap2 = googleMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
