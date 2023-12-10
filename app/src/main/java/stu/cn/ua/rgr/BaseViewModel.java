package stu.cn.ua.rgr;

import androidx.lifecycle.ViewModel;

import stu.cn.ua.rgr.model.NasaService;

public class BaseViewModel extends ViewModel {
    private NasaService nasaService;

    public BaseViewModel(NasaService nasaService){
        this.nasaService = nasaService;
    }

    protected final NasaService getNasaService(){
        return nasaService;
    }
}
