package stu.cn.ua.rgr.model;

import android.util.Log;

import com.annimon.stream.Stream;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import retrofit2.Response;
import stu.cn.ua.rgr.model.db.MeteoriteDao;
import stu.cn.ua.rgr.model.db.MeteoriteDbEntity;
import stu.cn.ua.rgr.model.network.MeteoriteNetworkEntity;
import stu.cn.ua.rgr.model.network.NasaApi;

public class NasaService {

    private ExecutorService executorService;
    private NasaApi nasaApi;
    private MeteoriteDao meteoriteDao;
    private String query = "";

    public static final String TAG = NasaService.class.getSimpleName();

    public NasaService(NasaApi nasaApi, MeteoriteDao meteoriteDao, ExecutorService executorService) {
        this.nasaApi = nasaApi;
        this.meteoriteDao = meteoriteDao;
        this.executorService = executorService;
    }

    public Cancellable getMeteorites(Callback<List<Meteorite>> callback) {
        Future<?> future = executorService.submit(() -> {
            try {
                List<MeteoriteDbEntity> entities = meteoriteDao.getMeteorites();
                List<Meteorite> meteorites = convertToMeteorites(entities);
                if (!meteorites.isEmpty()) {
                    callback.onResults(meteorites);
                }

                prepareQuery();

                Response<List<MeteoriteNetworkEntity>> response = nasaApi.getMeteorites(query).execute();
                if (response.isSuccessful()) {
                    List<MeteoriteDbEntity> newDbMeteorites = networkToDbEntities(response.body());
                    meteoriteDao.updateMeteorites(newDbMeteorites);
                    callback.onResults(convertToMeteorites(newDbMeteorites));
                } else {
                    if (meteorites.isEmpty()) {
                        RuntimeException exception = new RuntimeException("there is no such user");
                        Log.e(TAG, "ErrorFromNetwork!", exception);
                        callback.onError(exception);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error!", e);
                callback.onError(e);
            }
        });

        return () -> future.cancel(true);
    }

    public Cancellable getMeteoriteById(long id, Callback<Meteorite> callback) {
        Future<?> future = executorService.submit(() -> {
            try {
                MeteoriteDbEntity meteoriteDbEntity = meteoriteDao.getById(id);
                Meteorite meteorite = new Meteorite(meteoriteDbEntity);
                callback.onResults(meteorite);
            } catch (Exception e) {
                Log.e(TAG, "Error!", e);
                callback.onError(e);
            }
        });
        return () -> future.cancel(true);
    }

    private List<Meteorite> convertToMeteorites(List<MeteoriteDbEntity> entities) {
        return Stream.of(entities)
                .map(Meteorite::new)
                .toList();
    }

    private List<MeteoriteDbEntity> networkToDbEntities(List<MeteoriteNetworkEntity> entities) {
        return Stream.of(entities)
                .map(MeteoriteDbEntity::new)
                .toList();
    }

    private void prepareQuery() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -20);
        String formattedDate = dateFormat.format(calendar.getTime());

        query = "SELECT name, id, nametype, recclass, mass, fall, year, reclat, reclong, geolocation, :@computed_region_cbhk_fwbd, :@computed_region_nnqa_25f4" +
                " WHERE caseless_eq(fall, 'fell') AND (year > '" + formattedDate + "' :: floating_timestamp)";
    }
}
