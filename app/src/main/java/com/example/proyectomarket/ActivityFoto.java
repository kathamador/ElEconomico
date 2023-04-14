package com.example.proyectomarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ActivityFoto extends AppCompatActivity {
    ImageView img;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_GALLERY_IMG = 100;

    Button  galeria, camara, guardar;
    ImageButton regresar;
    String idagentecliente;
    String iac;
    Bitmap currentImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        SharedPreferences shared = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        mostrarFoto(idagentecliente);
        img = findViewById(R.id.foto);

        ImageButton regresar =  findViewById(R.id.btnRegresar);
        galeria = findViewById(R.id.Galeria);
        camara = findViewById(R.id.Camara);
        guardar = findViewById(R.id.Guardar);
        regresar.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
        galeria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_GALLERY_IMG);

        });
        camara.setOnClickListener(v -> {
            abrirCamara();
        });
        guardar.setOnClickListener(v -> {
            uploadData();
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
                            img.setImageBitmap(decodedImage);
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
    //para galeria

    //para camara
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
        else if (requestCode == RESULT_GALLERY_IMG && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getImagenBase64() {
        // Obtener la imagen del ImageView como Bitmap
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        // Convertir el Bitmap a Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imagenBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imagenBase64;
    }
    private void uploadData() {
        // Obtener los datos del formulario
        String imagenBase64 = getImagenBase64();

        // Realizar la solicitud al servidor
        String url = "https://finalproyect.com/eleconomico/updatephoto.php?idagentecliente="+idagentecliente;
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
                                Toast.makeText(ActivityFoto.this, message, Toast.LENGTH_SHORT).show();
                                // Reiniciar el formulario
                                img.setImageDrawable(null);

                            } else {
                                Toast.makeText(ActivityFoto.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityFoto.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("base64", imagenBase64);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
