package com.example.proyectomarket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ActivityActualizarDatos extends AppCompatActivity {
    BottomNavigationView nav;
    ImageView btnverperfil;
    String idagentecliente,telefono,contraseña,descripcion;
    String iac;

    TextView tvlatitud, tvlongitud;
    EditText  numero, direccion, contra;
    public static String latitud = "";
    public static String longitud = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos);
        nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.menuEmpleado);
        numero = findViewById(R.id.txtNumero);
        direccion = findViewById(R.id.txtDireccion);
        contra = findViewById(R.id.txtContra);
        tvlatitud = findViewById(R.id.txtlatitud);
        tvlongitud = findViewById(R.id.txtlongitud);


        SharedPreferences shared = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        btnverperfil = findViewById(R.id.imageView2);
        mostrarFoto(idagentecliente);
        Busqueda(idagentecliente);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        findViewById(R.id.btnsalvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Actualizar();
            }
        });

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menuProductos:
                        startActivity(new Intent(getApplicationContext(),ActivityInicioPro.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuPedidos:
                        startActivity(new Intent(getApplicationContext(),ContenedorFragment.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuEmpleado:
                        return true;
                }
                return false;
            }
        });
    }

    public void mostrarFoto(String idagentecliente) {
        iac = idagentecliente.trim();
        String url = "https://finalproyect.com/eleconomico/verimagen.php?idagentecliente=" + iac;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Decodificar la imagen base64 y establecerla en el ImageView
                        try {
                            byte[] imageBytes = Base64.decode(response, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            btnverperfil.setImageBitmap(decodedImage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // si hay un error, mostrarlo
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
///metodo para mostrar los datos de la base de datos de agente cliente
    private void Busqueda(String id) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final String ida = sharedPreferences.getString("idagentecliente", "");
        String url = "https://finalproyect.com/eleconomico/mostrarAct.php?idagentecliente=" + ida+ "";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        telefono= (jsonObject.getString("telefono"));
                        contraseña =(jsonObject.getString("contraseña"));
                        descripcion =(jsonObject.getString("descripcion"));
                        numero.setText(telefono);
                        contra.setText(contraseña);
                        direccion.setText(descripcion);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); // Agregar esta línea para imprimir la pila de errores
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void Actualizar()
    {
        String url = "https://finalproyect.com/eleconomico/actualizar.php";

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("idagentecliente", idagentecliente);
        params.put("telefono", numero.getText().toString().toLowerCase());
        params.put("contraseña", contra.getText().toString().toLowerCase());
        params.put("descripcion", direccion.getText().toString().toLowerCase());
        params.put("latitud", latitud);
        params.put("longitud", longitud);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Se ha Modificado exitosamente",Toast.LENGTH_SHORT).show();
                        numero.setText("");
                        contra.setText("");
                        direccion.setText("");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    //mostrar latitud y longitud
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Localizacion */
    public class Localizacion implements LocationListener {
        ActivityActualizarDatos mainActualizar;

        public ActivityActualizarDatos getMainActualizar() {
            return mainActualizar;
        }

        public void setMainActivity(ActivityActualizarDatos mainActualizar) {
            this.mainActualizar = mainActualizar;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas

            loc.getLatitude();
            loc.getLongitude();

            ActivityActualizarDatos.setLatitud(loc.getLatitude()+"");
            ActivityActualizarDatos.setLongitud(loc.getLongitude()+"");
            tvlatitud.setText(loc.getLatitude()+"");
            tvlongitud.setText(loc.getLongitude()+"");
            this.mainActualizar.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    public static void setLatitud(String latitud) {
        ActivityActualizarDatos.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        ActivityActualizarDatos.longitud = longitud;
    }

}