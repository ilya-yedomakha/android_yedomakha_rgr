package stu.cn.ua.rgr.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import stu.cn.ua.rgr.App;
import stu.cn.ua.rgr.R;
import stu.cn.ua.rgr.details.DetailsActivity;

public class MainActivity extends AppCompatActivity {
    private Button loadButton;
    private RecyclerView meteoritesList;
    private ProgressBar progress;
    private TextView emptyTextView;
    private TextView errorTextView;
    private MainViewModel viewModel;

    private MeteoritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadButton = findViewById(R.id.loadListButton);
        meteoritesList = findViewById(R.id.meteoritesList);
        progress = findViewById(R.id.progress);
        emptyTextView = findViewById(R.id.emptyTextView);
        errorTextView = findViewById(R.id.errorTextView);

        App app = (App) getApplication();
        ViewModelProvider viewModelProvider = new ViewModelProvider(this,app.getViewModelFactory());
        viewModel = viewModelProvider.get(MainViewModel.class);

        viewModel.getResults().observe(this, result -> {
            adapter.setMeteorites(result.getData());
            viewModel.setMeteoritesResult(result);
            switch (result.getStatus()) {
                case SUCCESS:
                    loadButton.setEnabled(true);
                    meteoritesList.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.GONE);                    
                    break;
                case EMPTY:
                    loadButton.setEnabled(true);
                    meteoritesList.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    errorTextView.setVisibility(View.GONE);
                    break;
                case LOADING:
                    loadButton.setEnabled(false);
                    meteoritesList.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.GONE);
                    break;
                case ERROR:
                    loadButton.setEnabled(true);
                    meteoritesList.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.VISIBLE);
                    break;
            }
        });
        
//        viewModel.getViewState().observe(this,state -> {
//            loadButton.setEnabled(state.isEnableSearchButton());
//            meteoritesList.setVisibility(toVisibility(state.isShowList()));
//            progress.setVisibility(toVisibility(state.isShowProgress()));
//            emptyTextView.setVisibility(toVisibility(state.isShowEmptyHint()));
//            errorTextView.setVisibility(toVisibility(state.isShowError()));
//
//            adapter.setMeteorites(state.getMeteorites());
//        });

        loadButton.setOnClickListener(v->{
            viewModel.getMeteorites();
        });

        initMeteoritesList();

    }

    private void initMeteoritesList(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        meteoritesList.setLayoutManager(layoutManager);
        adapter = new MeteoritesAdapter(meteorite -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_REPOSITORY_ID, meteorite.getId());
            startActivity(intent);
        });
        meteoritesList.setAdapter(adapter);
    }

    static int toVisibility(boolean show){
        return show ? View.VISIBLE : View.GONE;
    }
}