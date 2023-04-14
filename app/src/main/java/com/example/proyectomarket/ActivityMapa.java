package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityMapa extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    public static String nombre;
    public static String lo, la;
    private LocationManager locationManager;
    private GoogleMap mMap;
    double lat;
    double lon;
    Button mapa;
    private LatLng ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        ImageButton btnRegresar =  findViewById(R.id.btnRegresar);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nombre = sharedPreferences.getString("nombre", "");
        Busqueda(nombre);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        mapa = (Button) findViewById(R.id.ShowOnMap);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + la + "," + lo + " (" + "Destino" + ")";
                Uri location = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        findViewById(R.id.btnRegresar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });
    }

    private void Busqueda(String nombre) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String nom = sharedPreferences.getString("nombre", "");
        String url = "https://finalproyect.com/eleconomico/Mostrar.php?nombre=" + nom+ "";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        lo = (jsonObject.getString("longitud"));
                        la =(jsonObject.getString("latitud"));
                        lat = Double.parseDouble(la);
                        lon = Double.parseDouble(lo);
                        ubicacion = new LatLng(lat, lon);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (ubicacion != null) {
                        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicacion de "+nom));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
                    }
                }
            }
        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            //Toast.makeText(getApplicationContext(), "Error de conexion al buscar", Toast.LENGTH_SHORT).show();
            //Toast.makeText(ActivityMapa.this, ""+lat+" l"+lon, Toast.LENGTH_SHORT).show();
            error.printStackTrace(); // Agregar esta línea para imprimir la pila de errores
        }
    });

    RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    ///mapi
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Busqueda(nombre);
        //Toast.makeText(this, "z"+ubicacion, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
}