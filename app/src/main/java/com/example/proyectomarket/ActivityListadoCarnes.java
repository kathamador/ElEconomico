package com.example.proyectomarket;

import static com.example.proyectomarket.R.id.minus1;
import static com.example.proyectomarket.R.id.minusC;
import static com.example.proyectomarket.R.id.minusS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityListadoCarnes extends AppCompatActivity {
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    int count4 = 0;
    Button minus1, minus2, minus3,minus4;
    ImageButton btnregresar;
    Button plus1, plus2, plus3, plus4;
    TextView number1, number2,number3,number4;
    Button btn_agregarChuleta, btn_agregarPollo, btn_agregarCostilla,btn_agregarLomo;

    TextView producto1, producto2, producto3,producto4;
    double precio = 0;
    //double isv = 0.15;
    double subtotal= 0;
    double total = 0;
    String nombrep1, nombrep2,nombrep3,nombrep4;
    int cp1, cp2, cp3, cp4,idproducto;
    int cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_carnes);
        minus1 =findViewById(R.id.minus1);
        plus1 =findViewById(R.id.plus1);
        minus2 =findViewById(R.id.minus2);
        plus2 =findViewById(R.id.plus2);
        minus3 =findViewById(R.id.minus3);
        plus3 =findViewById(R.id.plus3);
        minus4 =findViewById(R.id.minus4);
        plus4 =findViewById(R.id.plus4);
        number1 =findViewById(R.id.number1);
        number2 =findViewById(R.id.number2);
        number3 =findViewById(R.id.number3);
        number4 =findViewById(R.id.number4);

        producto1 =(TextView)findViewById(R.id.textChuleta);
        producto2 =(TextView)findViewById(R.id.textPollo);
        producto3 =(TextView)findViewById(R.id.textCostilla);
        producto4 =(TextView)findViewById(R.id.textlomo);

        //cp1 = Integer.parseInt(number1.getText().toString());
        //cp2 = Integer.parseInt(number2.getText().toString());
        //cp3 = Integer.parseInt(number3.getText().toString());
       /// cp4 = Integer.parseInt(number4.getText().toString());
        nombrep1 = (String)producto1.getText();
        nombrep2 = (String)producto2.getText();
        nombrep3 = (String)producto3.getText();
        nombrep4 = (String)producto4.getText();

        btn_agregarChuleta = (Button) findViewById(R.id.btnagregarChuleta);
        btn_agregarPollo = (Button) findViewById(R.id.btnagregarPollo);
        btn_agregarCostilla = (Button) findViewById(R.id.btnagregarCostilla);
        btn_agregarLomo = (Button) findViewById(R.id.btnagregarLomo);


        btn_agregarChuleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos();
                agregarCarrito();
            }

        });

        btn_agregarPollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos2();
                agregarCarrito();
            }

        });

        btn_agregarCostilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos3();
                agregarCarrito();
            }

        });

        btn_agregarLomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos4();
                agregarCarrito();
            }

        });
        btnregresar=(ImageButton) findViewById(R.id.btn_regresar);

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        ActivityProductos.class);
                startActivity(intent);

            }
        });
        minus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if(c.getId()==R.id.plus1){
                    count1=count1 + 1;
                    //cantidad=count1;
                    setText1();

                } else if (c.getId()==R.id.minus1) {
                    count1 =count1 -1;
                    //cantidad=count1;
                    setText1();

                }

            }
        });
        plus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if(c.getId()==R.id.plus1){
                    count1=count1 + 1;
                    //cantidad=count1;
                    setText1();

                } else if (c.getId()==R.id.minus1) {
                    count1 =count1 -1;
                    //cantidad=count1;
                    setText1();

                }

            }
        });
        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View s) {
                if(s.getId()==R.id.plus2){
                    count2=count2 + 1;
                    //cantidad=count2;
                    setText2();

                } else if (s.getId()==R.id.minus2) {
                    count2 =count2 -1;
                   // cantidad=count2;
                    setText2();

                }

            }
        });
        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View s) {
                if(s.getId()==R.id.plus2){
                    count2=count2 + 1;
                    //cantidad=count2;
                    setText2();

                } else if (s.getId()==R.id.minus2) {
                    count2 =count2 -1;
                    ///cantidad=count2;
                    setText2();

                }

            }
        });
        minus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                if(a.getId()==R.id.plus3){
                    count3=count3 + 1;
                   // cantidad=count3;
                    setText3();

                } else if (a.getId()==R.id.minus3) {
                    count3 =count3 -1;
                    //cantidad=count3;
                    setText3();

                }

            }
        });
        plus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                if(a.getId()==R.id.plus3){
                    count3=count3 + 1;
                    ///cantidad=count3;
                    setText3();

                } else if (a.getId()==R.id.minus3) {
                    count3 =count3 -1;
                    //cantidad=count3;
                    setText3();

                }

            }
        });
        minus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View j) {
                if(j.getId()==R.id.plus4){
                    count4=count4 + 1;
                    //cantidad=count4;
                    setText4();

                } else if (j.getId()==R.id.minus4) {
                    count4 =count4 -1;
                   // cantidad=count4;
                    setText4();

                }

            }
        });
        plus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View j) {
                if(j.getId()==R.id.plus4){
                    count4=count4 + 1;
                   //cantidad=count4;
                    setText4();

                } else if (j.getId()==R.id.minus4) {
                    count4 =count4 -1;
                    //cantidad=count4;
                    setText4();

                }

            }
        });
    }
    //metodos

    public void productos()
    {
        if(nombrep1==nombrep1){
            idproducto=5;
            precio = 38;
            cantidad=count1;
            subtotal =(count1 * precio);
        }
        total = subtotal;
    }
    public void productos2()
    {

        if (nombrep2==nombrep2){
            idproducto=6;
            precio=40;
            cantidad=count2;
            subtotal =(count2 * precio);
        }
        total = subtotal;
    }
    public void productos3()
    {

        if (nombrep3==nombrep3){
            idproducto=7;
            precio=30;
            cantidad=count3;
            subtotal =(count3 * precio);
        }
        total = subtotal;
    }
    public void productos4()
    {
        if (nombrep4==nombrep4){
            idproducto=8;
            precio=38;
            cantidad=count4;
            subtotal =(count4 * precio);
        }
        total = subtotal;
    }

    public void setText1(){
        if(count1 <0){
            count1=0;
            number1.setText(Integer.toString(count1));
        }else {
            number1.setText(Integer.toString(count1));
        }
    }
    public void setText2(){
        if(count2<0){
            count2=0;
            number2.setText(Integer.toString(count2));
        }else {
            number2.setText(Integer.toString(count2));
        }
    }

    public void setText3(){
        if(count3<0){
            count3=0;
            number3.setText(Integer.toString(count3));
        }else {
            number3.setText(Integer.toString(count3));
        }
    }
    public void setText4(){
        if(count4<0){
            count4=0;
            number4.setText(Integer.toString(count4));
        }else {
            number4.setText(Integer.toString(count4));
        }
    }

    //metodo de agregar
    private void agregarCarrito() {
        // Obtener los datos del formulario
        // Realizar la solicitud al servidor
        String url = "https://finalproyect.com/eleconomico/createpro.php";
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
                                Toast.makeText(ActivityListadoCarnes.this, message, Toast.LENGTH_SHORT).show();
                                // Reiniciar el formulario

                            } else {
                                Toast.makeText(ActivityListadoCarnes.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityListadoCarnes.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //String idfactura ="1";
                params.put("idproducto", String.valueOf(idproducto));
               // params.put("idfactura", idfactura);
                params.put("cantidad", String.valueOf(cantidad));
                params.put("subtotal",String.valueOf(subtotal));
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

