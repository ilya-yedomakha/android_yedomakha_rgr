package stu.cn.ua.rgr.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import stu.cn.ua.rgr.BaseViewModel;
import stu.cn.ua.rgr.model.Callback;
import stu.cn.ua.rgr.model.Cancellable;
import stu.cn.ua.rgr.model.Meteorite;
import stu.cn.ua.rgr.model.NasaService;
import stu.cn.ua.rgr.model.Result;

public class DetailsViewModel extends BaseViewModel {

    private MutableLiveData<Result<Meteorite>> meteoriteLiveData = new MutableLiveData<>();

    {
        meteoriteLiveData.setValue(Result.empty());
    }

    private Cancellable cancellable;

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cancellable != null) cancellable.cancel();
    }

    public DetailsViewModel(NasaService nasaService) {
        super(nasaService);
    }

    public void loadMeteoriteById(long id) {
        meteoriteLiveData.setValue(Result.loading());
        cancellable = getNasaService().getMeteoriteById(id, new Callback<Meteorite>() {
            @Override
            public void onError(Throwable error) {
                meteoriteLiveData.postValue(Result.error(error));
            }

            @Override
            public void onResults(Meteorite data) {
                meteoriteLiveData.postValue(Result.success(data));
            }
        });
    }

    public LiveData<Result<Meteorite>> getResults() {
        return meteoriteLiveData;
    }
}
