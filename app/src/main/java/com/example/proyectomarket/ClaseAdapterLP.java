package com.example.proyectomarket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClaseAdapterLP extends Fragment {

    private ListView listViewPedidos;
    String estado;
    TextView textViewEstadoPedidoValor;
    String estadoPedido;
    Button btncalificar;
    ImageButton regresar;
    String idagentecliente;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_lista_pedidos, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String idagentecliente = sharedPreferences.getString("idagentecliente","");
        Log.e("SI-","VALORE"+idagentecliente);
        regresar = (ImageButton) view.findViewById(R.id.btn_regresar);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        listViewPedidos = view.findViewById(R.id.listapedidos);

        // Hacer la petición al servidor
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://finalproyect.com/eleconomico/mispedidos.php?idagentecliente="+idagentecliente ;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Crear un ArrayList de objetos Pedido
                            ArrayList<Personas> pedidos = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String estadoPedido = jsonObject.getString("Estado_pedido");
                                String fecha = jsonObject.getString("fecha");
                                String total = jsonObject.getString("total");
                                String nombre = jsonObject.getString("nombre");
                                String telefono = jsonObject.getString("telefono");
                                Integer idpedido  =  jsonObject.getInt("idpedidos");
                                Integer idcliente  =  jsonObject.getInt("idagentecliente");

                                // if que consulte si estado pedido tiene un valor = o que me diga su estado en texto

                                // estado="en proceso
                                // estado="en proceso
                                float calificacion = Float.parseFloat(jsonObject.getString("calificacion"));
                                pedidos.add(new Personas(idpedido,estadoPedido, calificacion,fecha,total,nombre,telefono,idcliente));

                            }

                            // Crear un adaptador personalizado para el ListView
                            PedidosAdapter pedidosAdapter = new PedidosAdapter(getActivity(), pedidos);
                            listViewPedidos.setAdapter(pedidosAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error en la petición: " + error.getMessage());
                    }
                });

        // Agregar la petición al RequestQueue
        queue.add(jsonArrayRequest);

        return view;
    }

    private class PedidosAdapter extends ArrayAdapter<Personas> {
        private Context context;
        private ArrayList<Personas> pedidos;

        public PedidosAdapter(Context context, ArrayList<Personas> pedidos) {
            super(context, R.layout.activity_fragment_lista_p, pedidos);
            this.context = context;
            this.pedidos = pedidos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.activity_fragment_lista_p, parent, false);

            // Obtener las referencias a los elementos de la fila
            TextView id = rowView.findViewById(R.id.pedido);
            ///TextView textViewEstadoPedido = rowView.findViewById(R.id.estado);
            textViewEstadoPedidoValor = rowView.findViewById(R.id.estado);
            TextView fecha = rowView.findViewById(R.id.fecha);
            RatingBar ratingBarCalificacion = rowView.findViewById(R.id.ratingBar);
            // Asignar los valores a los elementos de la fila
            if(pedidos.get(position).getEstadoPedido().equals("0")){
                textViewEstadoPedidoValor.setText("Estado: En Proceso");
                ratingBarCalificacion.setEnabled(false);
            }else if(pedidos.get(position).getEstadoPedido().equals("1")){
                textViewEstadoPedidoValor.setText("Estado: Confirmado");
                ratingBarCalificacion.setEnabled(false);
            }else if(pedidos.get(position).getEstadoPedido().equals("2")){
                textViewEstadoPedidoValor.setText("Estado: En Camino");
                ratingBarCalificacion.setEnabled(false);
            }else if(pedidos.get(position).getEstadoPedido().equals("3")){
                textViewEstadoPedidoValor.setText("Estado: Entregado");
                ratingBarCalificacion.setEnabled(true);

            }

            mQueue = Volley.newRequestQueue(context);
            ratingBarCalificacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    int calificacion = Math.round(rating);
                    Integer idPedido = pedidos.get(position).getIdpedido();
                    Log.e("Se","hizo clic"+calificacion);

                    String url = "https://finalproyect.com/eleconomico/updatecalificacion.php?id=" + idPedido + "&calificacion="+calificacion;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    // actualizar tabla pedidos aquí
                                    Log.e("Correct","llama apirest");
                                } else {
                                    Log.e("error.-","error");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("error.-","error de servidor");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("error.-","error de conexión ");
                        }
                    });

                    mQueue.add(jsonObjectRequest);
                }
            });


            // Asignar los valores a los elementos de la fila
            id.setText("Pedido #"+pedidos.get(position).getIdpedido());
            //textViewEstadoPedidoValor.setText(pedidos.get(position).getEstadoPedido());
            fecha.setText(pedidos.get(position).getFecha());
            ratingBarCalificacion.setRating(pedidos.get(position).getCalificacion());
            Intent intent = new Intent(getContext(), ActivityFragmentListaP.class);
            intent.putExtra("IdPedido", pedidos.get(position).getIdpedido());


            LinearLayout linearLayoutContent = rowView.findViewById(R.id.content);

            linearLayoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear el Intent para la siguiente Activity y agregar los datos que deseas pasar

                    //debo modificar la clase "persona"
                    Intent intent = new Intent(getContext(), ViewPedidos.class);
                    intent.putExtra("IdPedido", pedidos.get(position).getIdpedido());
                    intent.putExtra("Total", pedidos.get(position).getTotal());
                    intent.putExtra("Nombre", pedidos.get(position).getNombre());
                    intent.putExtra("Telefono", pedidos.get(position).getTelefono());
                    intent.putExtra("IdCliente", pedidos.get(position).getIdcliente());
                    intent.putExtra("EstadoPedido", pedidos.get(position).getEstadoPedido());
                    // Iniciar la siguiente Activity
                    getContext().startActivity(intent);
                }
            });
            return rowView;
        }
    }

}
