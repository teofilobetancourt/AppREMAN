package com.appreman.app.Api;

import com.appreman.app.Api.Response.AsignarResponse;
import com.appreman.app.Api.Response.OperadorResponse;
import com.appreman.app.Api.Response.RespuestaResponse;
import com.appreman.app.Api.Response.ActualizarRespuestaResponse;
import com.appreman.app.Api.Response.ActualizarAsignarResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServices {

    @Headers({ "Accept: application/json;charset=UTF-8"})
    @GET("operador-lectura/")
    Call<OperadorResponse> getOperadores(
            //@Header("Authorization") String auth
    );

    @Headers({ "Accept: application/json;charset=UTF-8"})
    @GET("asignar-lectura/")
    Call<AsignarResponse> getAsignaciones(
            //@Header("Authorization") String auth
    );

    @Headers({ "Accept: application/json;charset=UTF-8"})
    @GET("respuesta-lectura/")
    Call<RespuestaResponse> getRespuestas(
            //@Header("Authorization") String auth
    );

    @Headers({ "Accept: application/json;charset=UTF-8"})
    @POST("respuesta-actualiza/")
    Call<ActualizarRespuestaResponse> postActualizarRespuesta(
            @Body RequestBody actualizarRespuesta
    );

    @Headers({ "Accept: application/json;charset=UTF-8"})
    @POST("asignar-actualizar/")
    Call<ActualizarAsignarResponse> postActualizarAsignar(
            @Body RequestBody actualizarAsignar
    );
}