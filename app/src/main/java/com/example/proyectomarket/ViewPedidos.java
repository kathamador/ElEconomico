package com.example.proyectomarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import java.util.List;

public class ViewPedidos extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    private RequestQueue mQueue;
    double acumsubtotal=0;
    private ListView listView;

    ImageButton btnregresar;
    private List<Factura> facturas;
    private Button btnEntregarPedido;
    //public static String nombre;
    private LocationManager locationManager;
    private GoogleMap mMap;
    double longitud;
    double latitud;
    Button mapa;
    private LatLng ubicacion;
    int idPedido;
    private String channelID = "canal";
    private String channelIDT3 = "canal";
    private PendingIntent pendingIntent;
    String nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pedidos);
        btnregresar= (ImageButton) findViewById(R.id.btnregresar);

        //mapa
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        mapa = (Button) findViewById(R.id.ShowOnMap);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + latitud + "," + longitud + " (" + "Destino" + ")";
                Uri location = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        // Obtener los datos pasados como extras
         idPedido = getIntent().getIntExtra("IdPedido", 0);
        int IdCliente = getIntent().getIntExtra("IdCliente", 0);
        String total = getIntent().getStringExtra("Total");
        nombre = getIntent().getStringExtra("Nombre");
        String telefono = getIntent().getStringExtra("Telefono");
        String EstadoPedido = getIntent().getStringExtra("EstadoPedido");


        TextView txtPedido = findViewById(R.id.txtPedidos);
        TextView txtTotal = findViewById(R.id.txtTotal);
        TextView txtNombreCliente = findViewById(R.id.txtNombreCliente);
        TextView txtTelefono = findViewById(R.id.txtTelefono);
        TextView txtSubtotalSuma = findViewById(R.id.txtSubtotal);  //subtotal suma


        txtPedido.setText("Pedido #"+String.valueOf(idPedido));
        txtTotal.setText(total);
        txtNombreCliente.setText(nombre);
        txtTelefono.setText(telefono);

        if (EstadoPedido.equals("2")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotification();
            } else {
                showNewNotificacion();
            }
        } else if (EstadoPedido.equals("3")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotificationT3();
            } else {
                showNewNotificacionT3();
            }
        } else {
        }

        mQueue = Volley.newRequestQueue(this);

        listView = findViewById(R.id.listProductos);
        facturas = new ArrayList<>();

        // Crear la cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(this);

        // URL del servicio web
        String url = "https://finalproyect.com/eleconomico/viewProducto.php?idcliente="+IdCliente+"&idpedidos="+idPedido;

        // Crear la petición GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear el JSON
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String nombreProducto = jsonObject.getString("nombre");
                                int cantidad = jsonObject.getInt("total_cantidad");
                                double precio = jsonObject.getDouble("precio");
                                double subtotal = jsonObject.getDouble("subtotal");
                                double totalsub = jsonObject.getDouble("total_subtotal");
                                latitud = jsonObject.getDouble("latitud");
                                longitud = jsonObject.getDouble("longitud");
                                ubicacion = new LatLng(latitud, longitud);

                                // Crear un objeto Factura con los datos obtenidos
                                Factura factura = new Factura(nombreProducto, cantidad, precio, subtotal,totalsub,latitud,longitud);
                                facturas.add(factura);

                                acumsubtotal+=totalsub;
                                txtSubtotalSuma.setText(String.valueOf(acumsubtotal));
                                if (ubicacion != null) {
                                    mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicacion repartidor"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
                                }
                            }
                            // Crear un adapter para el ListView
                            FacturaAdapter adapter = new FacturaAdapter(ViewPedidos.this, facturas);
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewPedidos.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la petición a la cola de peticiones
        queue.add(stringRequest);
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ContenedorFragment.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
    //notificaciones push
    private void showNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            showNewNotificacion();
        }
    }
    private void showNotificationT3() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelIDT3, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            showNewNotificacionT3();
        }
    }
    private void showNewNotificacion() {
        setPendingIntent(ActivityNoti.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelID).setSmallIcon(R.drawable.perfiluser)
                .setContentTitle("Notificacion de su pedido")
                .setContentText("su pedido "+idPedido+" esta en camino "+nombre)
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
    private void showNewNotificacionT3() {
        setPendingIntent(ActivityNoti.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelIDT3).setSmallIcon(R.drawable.perfiluser)
                .setContentTitle("Notificacion de su pedido")
                .setContentText("Su pedido "+idPedido+" esta entregado "+nombre)
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
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
