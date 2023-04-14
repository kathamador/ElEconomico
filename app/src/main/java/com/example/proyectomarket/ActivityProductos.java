package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ActivityProductos extends AppCompatActivity {

    BottomNavigationView nav;
    LinearLayout bebidas,carnes,lacteos,bebes,frutasVerduras;
    int randomNumber;
    String fechaA;
    String id;
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    ImageButton cerrar,btnverpedido;

    ImageView btnverperfil;
    String idagentecliente;
    String iac;
    Button mostrarNotificacion;
    private String channelID = "canal";
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        bebidas=findViewById(R.id.layoutBebidas);
        carnes=findViewById(R.id.layoutCarnes);
        lacteos=findViewById(R.id.layoutLacteos);
        frutasVerduras=findViewById(R.id.layoutFrutasVerduras);
        bebes=findViewById(R.id.layoutBebes);
        nav=findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.menuProductos);
        cerrar=  findViewById(R.id.btncerrarc);
        btnverpedido=  findViewById(R.id.btnverpedidos);

        SharedPreferences shared = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        idagentecliente = shared.getString("idagentecliente", "");
        btnverperfil = findViewById(R.id.imageCliente);
        mostrarFoto(idagentecliente);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "");
        if (!nombre.isEmpty()) {
            TextView nombreTextView = findViewById(R.id.textNombre);
            nombreTextView.setText(nombre);
        }

        Random random = new Random();
        randomNumber = random.nextInt(9000) + 1000;
       Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
       fechaA = dateFormat.format(calendar.getTime());
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         //fechaA = dateFormat.format(date);
        // de la línea 32 a 37 se debe copiar en cada activity que necesite.
        // si necesita usar el nombre para otra función, tome en cuenta que la variable String nombre, tiene el valor
         id = sharedPreferences.getString("idagentecliente", "");

        btnverperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialogoFragmentPerfil myDialogoFragment = new MyDialogoFragmentPerfil();
                myDialogoFragment.show(getSupportFragmentManager(),"PantallaPerfil");
            }
        });
        bebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ActivityListadoBebidas.class);
                startActivity(intent);

            }
        });
        btnverpedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ContenedorFragment.class);
                startActivity(intent);
            }
        });

        carnes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ActivityListadoCarnes.class);
                startActivity(intent);

            }
        });

        lacteos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ActivityListadoLacteos.class);
                startActivity(intent);

            }
        });

        frutasVerduras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ActivityListadoFrutasVerduras.class);
                startActivity(intent);

            }
        });

        bebes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ActivityProductos.this,ActivityListadoBebes.class);
                startActivity(intent);

            }
        });

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
        //segundoBoton.setEnabled(false);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProductos.this);

                //Crear ImageView personalizado
                ImageView image = new ImageView(ActivityProductos.this);
                image.setImageResource(R.drawable.logocerrarcompra); //reemplaza "imagen_dialogo" con el nombre de tu imagen
                image.setScaleType(ImageView.ScaleType.FIT_XY); //escala la imagen para ajustarla al tamaño del ImageView
                image.setAdjustViewBounds(true); //ajusta la vista para que se ajuste al tamaño de la imagen

                // Establecer la vista personalizada del diálogo
                builder.setView(image);
                builder.setTitle("Compra exitosa");
                builder.setMessage("Gracias por su compra");
                //builder.setPositiveButton("Aceptar", null);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            showNotification();
                        } else {
                            showNewNotificacion();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }
    private void cerrarCarrito() {
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
                                Toast.makeText(ActivityProductos.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityProductos.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
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
                params.put("idagentecliente", id);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Toast.makeText(getApplicationContext(), "Añadido al Carrito", Toast.LENGTH_SHORT).show();
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
    //notificacion push
    private void showNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            showNewNotificacion();
        }
    }

    private void showNewNotificacion() {
        setPendingIntent(ActivityNoti.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID).setSmallIcon(R.drawable.perfiluser)
                .setContentTitle("Notificacion de su pedido")
                .setContentText("su pedido esta en proceso")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        managerCompat.notify(1, builder.build());
    }
    private void setPendingIntent(Class<?> clsActivity){
        Intent intent = new Intent(this,clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
    }

}