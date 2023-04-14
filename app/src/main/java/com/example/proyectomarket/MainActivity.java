package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav;
    TextView nombre;
    Bundle bundle;
    ImageView btnverperfil;
    String idagentecliente;
    String iac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageSlider imageSlider1 = findViewById(R.id.inicio);
        ImageSlider imageSlider2 = findViewById(R.id.slider2);
        ImageSlider imageSlider3 = findViewById(R.id.slider3);
        ImageSlider imageSlider4 = findViewById(R.id.inicio2);


        ArrayList<SlideModel> images1 = new ArrayList<>();
        images1.add(new SlideModel(R.drawable.slider1,null));
        imageSlider1.setImageList(images1, ScaleTypes.CENTER_CROP);


        ArrayList<SlideModel> images2 = new ArrayList<>();
        images2.add(new SlideModel(R.drawable.squeso,null));
        images2.add(new SlideModel(R.drawable.spollo,null));
        images2.add(new SlideModel(R.drawable.saguacate,null));
        imageSlider2.setImageList(images2, ScaleTypes.CENTER_CROP);

        ArrayList<SlideModel> images3 = new ArrayList<>();
        images3.add(new SlideModel(R.drawable.shuevo,null));
        images3.add(new SlideModel(R.drawable.span,null));
        images3.add(new SlideModel(R.drawable.schile,null));
        imageSlider3.setImageList(images3, ScaleTypes.CENTER_CROP);

        ArrayList<SlideModel> images4 = new ArrayList<>();
        images4.add(new SlideModel(R.drawable.slider2,null));
        imageSlider4.setImageList(images4, ScaleTypes.CENTER_CROP);

        SharedPreferences shared = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        btnverperfil = findViewById(R.id.imageCliente);
        mostrarFoto(idagentecliente);

        nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.menuHome);
        nombre = (TextView) findViewById(R.id.textNombre);
        // de la línea 32 a 37 se debe copiar en cada activity que necesite.
        // si necesita usar el nombre para otra función, tome en cuenta que la variable String nombre, tiene el valor
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "apellido");
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
}
    public void busquedaCliente(String correo)
    {
        bundle = getIntent().getExtras();
        correo = (bundle.getString("correo").toString());

        String url = "https://finalproyect.com/eleconomico/usuario.php?texto=" + correo;
        HashMap<String, String> params = new HashMap<String, String>();

        // params.put("Image", image);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        params.put("nombre", nombre.getText().toString().toLowerCase());
                        nombre.setText(params.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                ///AlertaDialogo("Error al modificar "+error.getMessage(),"Error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
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