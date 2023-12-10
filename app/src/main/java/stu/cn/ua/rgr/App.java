package stu.cn.ua.rgr;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import stu.cn.ua.rgr.model.NasaService;
import stu.cn.ua.rgr.model.db.AppDatabase;
import stu.cn.ua.rgr.model.db.MeteoriteDao;
import stu.cn.ua.rgr.model.network.NasaApi;

public class App extends Application {

    private ViewModelProvider.Factory viewModelFactory;

    private static final String BASE_URL = "https://data.nasa.gov/";


    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        NasaApi nasaApi = retrofit.create(NasaApi.class);

        ExecutorService executorService = Executors.newCachedThreadPool();

        AppDatabase appDatabase = Room.databaseBuilder(this, AppDatabase.class, "database2.db")
                .build();
        MeteoriteDao meteoriteDao = appDatabase.getMeteoriteDao();

        NasaService nasaService = new NasaService(nasaApi, meteoriteDao, executorService);
        viewModelFactory = new ViewModelFactory(nasaService);

    }

    public ViewModelProvider.Factory getViewModelFactory() {
        return viewModelFactory;
    }
}
