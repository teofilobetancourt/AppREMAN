package com.appreman.app.Sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.appreman.app.Api.ApiAdapter;
import com.appreman.app.Api.ApiServices;
import com.appreman.app.Api.Response.AsignarResponse;
import com.appreman.app.Models.Asignar;
import com.appreman.app.Utils.Constant;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private ApiServices appServices;
    private List<Asignar> asignacionesArray = new ArrayList<>();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        // Initialize ApiServices here
        this.appServices = new ApiAdapter().getApiService(Constant.URL);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        actualizarAsignaciones();
    }

    public void actualizarAsignaciones() {
        Call<AsignarResponse> callSync = appServices.getAsignaciones();

        try {
            Response<AsignarResponse> response = callSync.execute();

            if (response.isSuccessful() && response.body() != null) {
                List<Asignar> asignaciones = response.body().getAsignaciones();

                if (asignaciones != null && !asignaciones.isEmpty()) {
                    asignacionesArray.clear();
                    asignacionesArray.addAll(asignaciones);
                }
            } else {
                handleErrorResponse(response);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error executing call: ", e);
        }
    }

    private void handleErrorResponse(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                Log.e(TAG, "Error response: " + jObjError.toString());
            } else {
                Log.e(TAG, "Unknown error occurred. Response code: " + response.code());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing error response: ", e);
        }
    }

    public List<Asignar> getAsignacionesArray() {
        return asignacionesArray;
    }
}