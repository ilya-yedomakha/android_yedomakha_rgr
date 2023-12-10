package stu.cn.ua.rgr.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import stu.cn.ua.rgr.BaseViewModel;
import stu.cn.ua.rgr.model.Callback;
import stu.cn.ua.rgr.model.Cancellable;
import stu.cn.ua.rgr.model.Meteorite;
import stu.cn.ua.rgr.model.NasaService;
import stu.cn.ua.rgr.model.Result;

public class MainViewModel extends BaseViewModel {

    private Result<List<Meteorite>> meteoritesResult = Result.empty();

    public Result<List<Meteorite>> getMeteoritesResult() {
        return meteoritesResult;
    }

    public void setMeteoritesResult(Result<List<Meteorite>> meteoritesResult) {
        this.meteoritesResult = meteoritesResult;
    }

    private MutableLiveData<Result<List<Meteorite>>> meteoriteLiveData = new MutableLiveData<>();

    {
        meteoriteLiveData.postValue(Result.empty());
    }

    private Cancellable cancellable;

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cancellable != null) cancellable.cancel();
    }

    public MainViewModel(NasaService nasaService) {
        super(nasaService);
    }

    public void getMeteorites() {
        meteoriteLiveData.setValue(Result.loading());
        cancellable = getNasaService().getMeteorites(new Callback<List<Meteorite>>() {
            @Override
            public void onError(Throwable error) {
                if (meteoritesResult.getStatus() != Result.Status.SUCCESS) {
                    meteoriteLiveData.postValue(Result.error(error));
                }
            }

            @Override
            public void onResults(List<Meteorite> data) {
                meteoriteLiveData.postValue(Result.success(data));
            }
        });
    }

    public LiveData<Result<List<Meteorite>>> getResults() {
        return meteoriteLiveData;
    }
}
