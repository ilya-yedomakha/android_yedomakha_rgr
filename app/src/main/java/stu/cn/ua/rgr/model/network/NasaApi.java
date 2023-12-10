package stu.cn.ua.rgr.model.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApi {
    @GET("resource/gh4g-9sfh.json")
    Call<List<MeteoriteNetworkEntity>> getMeteorites(@Query("$query") String query);
}
