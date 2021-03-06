package com.example.appviveresprimavera.webservice;

import com.example.appviveresprimavera.entidades.Proveedor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ClienteApi {

    String BASE_URL="http://192.168.0.105:8000/";

    @GET("proveedores/")
    public Call<List<Proveedor>> obtenerTodos();

    @GET("proveedores/{codigo}")
    public Call<Proveedor> obtenerPorCodigo(@Path("codigo") String codigo);



}
