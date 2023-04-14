package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class MyDialogoFragmentPerfil extends DialogFragment {

    Button btnubicacion, foto;
    ImageButton regresar;
    TextView nombre,direccion,apellidok,btncerrar;
    ImageView btnverperfil;
    String idagentecliente;
    String iac;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.popup_enviar, container, false);
        btnubicacion = (Button) view.findViewById(R.id.ubicacion);
        nombre = (TextView) view.findViewById(R.id.nombre);
        //direccion = (TextView) view.findViewById(R.id.direccion);
        apellidok = (TextView) view.findViewById(R.id.apellido);
        ImageButton regresar = view.findViewById(R.id.regresar);
        btncerrar = (Button) view.findViewById(R.id.cerrarS);
        foto = (Button) view.findViewById(R.id.modi);

        SharedPreferences shared = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        btnverperfil = view.findViewById(R.id.imageView);
        mostrarFoto(idagentecliente);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "");
        if (!nombre.isEmpty()) {
            TextView nombreTextView = view.findViewById(R.id.nombre);
            nombreTextView.setText(nombre);
        }
        String apellidoc = sharedPreferences.getString("apellido", "");
        if (!apellidoc.isEmpty()) {
            TextView apellido = view.findViewById(R.id.apellido);
            apellido.setText(apellidoc);
        }

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityFoto.class);
                startActivity(intent);
            }
        });
        btncerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cierre de Sesion");
                builder.setMessage("¿Quieres cerrar la app?");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(getContext(),Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnubicacion.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ActivityMapa.class);
                        startActivity(intent);
                    }
                }
        );
        regresar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
        return view;
    }
    public void busquedaCliente(String correo)
    {
        String url = "https://finalproyect.com/eleconomico/mostrarU.php?texto=" + correo;
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}