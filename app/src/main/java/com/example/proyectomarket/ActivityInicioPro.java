package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ActivityInicioPro extends AppCompatActivity {
    TextView nombre;
    ImageView btnverperfil;
    String idagentecliente;
    BottomNavigationView nav;
    String iac;
    ImageButton comprar;
    int randomNumber;
    String fechaA;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_pro);
        comprar=findViewById(R.id.btncomprar);
        nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.menuProductos);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuProductos:
                        startActivity(new Intent(getApplicationContext(),ActivityInicioPro.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuPedidos:
                        startActivity(new Intent(getApplicationContext(),ContenedorFragment.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuEmpleado:
                        startActivity(new Intent(getApplicationContext(),ActivityActualizarDatos.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        Random random = new Random();
        randomNumber = random.nextInt(9000) + 1000;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fechaA = dateFormat.format(calendar.getTime());

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCarrito();
                Intent intent= new Intent(ActivityInicioPro.this,ActivityProductos.class);
                startActivity(intent);
            }
        });

        SharedPreferences shared = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        btnverperfil = findViewById(R.id.imageCliente);
        mostrarFoto(idagentecliente);
        nombre = (TextView) findViewById(R.id.textNombre);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "");
        if (!nombre.isEmpty()) {
            TextView nombreTextView = findViewById(R.id.textNombre);
            nombreTextView.setText(nombre);
        }
        btnverperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogoFragmentPerfil myDialogoFragment = new MyDialogoFragmentPerfil();
                myDialogoFragment.show(getSupportFragmentManager(),"PantallaPerfil");
            }
        });

    }
    private void iniciarCarrito() {
        // Realizar la solicitud al servidor
        String url = "https://finalproyect.com/eleconomico/createfactura.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int success = jsonResponse.getInt("success");
                            String message = jsonResponse.getString("message");
                            if (success == 1) {
                                // Toast.makeText(ActivityProductos.this, message, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ActivityInicioPro.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityInicioPro.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //crear un resapi que nos envie la ultima factura creada a traves de una variable entera
                //String idfactura=Ultimafactura();
                //String idfactura ="4";
                params.put("num_comprobante",String.valueOf(randomNumber));
                params.put("fecha", fechaA);
                params.put("idagentecliente", idagentecliente);
                return params;
            }
        };
        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Toast.makeText(getApplicationContext(), "Bienvenido a Supermercado El Economico. \n Disfrute su compra!", Toast.LENGTH_SHORT).show();
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
}