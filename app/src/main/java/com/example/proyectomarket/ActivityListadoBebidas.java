package com.example.proyectomarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class ActivityListadoBebidas extends AppCompatActivity {
    int countC = 0;
    int countS = 0;
    int countA = 0;
    int countJ = 0;
    Button minusC, minusS, minusA,minusJ;
    Button  btnagregarc, btnagregars,btnagregara,btnagregarj;
    ImageButton btnregresar;
    Button plusC, plusS, plusA, plusJ;
    TextView numberC, numberS,numberA,numberJ;
    TextView producto1, producto2, producto3,producto4;
    double precio = 0;
    //double isv = 0.15;
    double subtotal= 0;
    double total = 0;
    String nombrec, nombrej,nombrea,nombres;
    int cc, cs, ca, cj,idproducto;
    int cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_bebidas);
//        nombrecliente=getIntent().getExtras().getString("nombre");
        minusC =findViewById(R.id.minusC);
        plusC =findViewById(R.id.plusC);
        minusS =findViewById(R.id.minusS);
        plusS =findViewById(R.id.plusS);
        minusJ =findViewById(R.id.minusJ);
        plusJ =findViewById(R.id.plusJ);
        minusA =findViewById(R.id.minusA);
        plusA =findViewById(R.id.plusA);
        numberC =(TextView)findViewById(R.id.numberC);
        numberS =(TextView)findViewById(R.id.numberS);
        numberJ =(TextView)findViewById(R.id.numberJ);
        numberA =(TextView)findViewById(R.id.numberA);
        producto1 =(TextView)findViewById(R.id.textCorona);
        producto2 =(TextView)findViewById(R.id.textSprite);
        producto3 =(TextView)findViewById(R.id.textAgua);
        producto4 =(TextView)findViewById(R.id.textJugo);

        //cc = Integer.parseInt(numberC.getText().toString());
        //cs = Integer.parseInt(numberS.getText().toString());
        //cj = Integer.parseInt(numberJ.getText().toString());
        //ca = Integer.parseInt(numberA.getText().toString());
        nombrec = (String)producto1.getText();
        nombres = (String)producto2.getText();
        nombrea = (String)producto3.getText();
        nombrej = (String)producto4.getText();

        btnregresar=(ImageButton)  findViewById(R.id.btn_regresar);
        btnagregarc=(Button) findViewById(R.id.btnagregarc);
        btnagregars=(Button) findViewById(R.id.btnagregarSprite);
        btnagregara=(Button) findViewById(R.id.btnagregarAgua);
        btnagregarj=(Button) findViewById(R.id.btnagregarJ);


        btnagregarc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos();
                agregarCarrito();
            }

        });

        btnagregars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos2();
                agregarCarrito();
            }

        });

        btnagregara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos3();
                agregarCarrito();
            }

        });

        btnagregarj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productos4();
                agregarCarrito();
            }

        });
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        ActivityProductos.class);
                startActivity(intent);

            }
        });
        minusC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if(c.getId()==R.id.plusC){
                    countC=countC + 1;
                    //cantidad = countC;
                    setTextC();

                } else if (c.getId()==R.id.minusC) {
                    countC =countC -1;
                    ///cantidad = countC;
                    setTextC();

                }

            }
        });
        plusC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                if(c.getId()==R.id.plusC){
                    countC=countC + 1;
                    //cantidad = countC;
                    setTextC();

                } else if (c.getId()==R.id.minusC) {
                    countC =countC -1;
                    //cantidad = countC;
                    setTextC();

                }

            }
        });
        minusS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View s) {
                if(s.getId()==R.id.plusS){
                    countS=countS + 1;
                    //cantidad = countS;
                    setTextS();

                } else if (s.getId()==R.id.minusS) {
                    countS =countS -1;
                    //cantidad = countS;
                    setTextS();

                }

            }
        });
        plusS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View s) {
                if(s.getId()==R.id.plusS){
                    countS=countS + 1;
                    ///cantidad = countS;
                    setTextS();

                } else if (s.getId()==R.id.minusS) {
                    countS =countS -1;
                   /// cantidad = countS;
                    setTextS();

                }

            }
        });
        minusA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                if(a.getId()==R.id.plusA){
                    countA=countA + 1;
                    cantidad = countA;
                    setTextA();

                } else if (a.getId()==R.id.minusA) {
                    countA =countA -1;
                    cantidad = countA;
                    setTextA();

                }

            }
        });
        plusA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                if(a.getId()==R.id.plusA){
                    countA=countA + 1;
                    cantidad = countA;
                    setTextA();

                } else if (a.getId()==R.id.minusA) {
                    countA =countA -1;
                    cantidad = countA;
                    setTextA();

                }

            }
        });
        minusJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View j) {
                if(j.getId()==R.id.plusJ){
                    countJ=countJ + 1;
                    cantidad = countJ;
                    setTextJ();

                } else if (j.getId()==R.id.minusJ) {
                    countJ =countJ -1;
                    cantidad = countJ;
                    setTextJ();

                }

            }
        });
        plusJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View j) {
                if(j.getId()==R.id.plusJ){
                    countJ=countJ + 1;
                    cantidad = countJ;
                    setTextJ();

                } else if (j.getId()==R.id.minusJ) {
                    countJ =countJ -1;
                    cantidad = countJ;
                    setTextJ();

                }

            }
        });
    }
//metodos

    public void productos()
    {
        if(nombrec==nombrec){
            idproducto=1;
            precio = 400;
            cantidad=countC;
            subtotal =(countC * precio);
        }
        total = subtotal;
    }
    public void productos2()
    {

        if (nombres==nombres){
            idproducto=2;
            precio=50;
            cantidad=countS;
            subtotal =(countS * precio);
        }
        total = subtotal;
    }
    public void productos3()
    {

         if (nombrea==nombrea){
            idproducto=3;
            precio=30;
            cantidad=countA;
            subtotal =(countA * precio);
        }
        total = subtotal;
    }
    public void productos4()
    {
        if (nombrej==nombrej){
            idproducto=4;
            precio=38;
            cantidad=countJ;
            subtotal =(countJ * precio);
        }
        total = subtotal;
    }
    public void setTextC(){
            if(countC<0){
                countC=0;
                numberC.setText(Integer.toString(countC));
            }else {
                numberC.setText(Integer.toString(countC));
            }
    }
    public void setTextS(){
            if(countS<0){
                countS=0;
                numberS.setText(Integer.toString(countS));
            }else {
                numberS.setText(Integer.toString(countS));
            }
    }

    public void setTextJ(){
            if(countJ<0){
                countJ=0;
                numberJ.setText(Integer.toString(countJ));
            }else {
                numberJ.setText(Integer.toString(countJ));
            }
    }
    public void setTextA(){
            if(countA<0){
                countA=0;
                numberA.setText(Integer.toString(countA));
            }else {
                numberA.setText(Integer.toString(countA));
            }
    }
    //metodo de agregar
    private void agregarCarrito() {
        // Obtener los datos del formulario

        ///longitud = tvlatitud.getText().toString();
        ///latitud = tvlatitud.getText().toString();


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
                                Toast.makeText(ActivityListadoBebidas.this, message, Toast.LENGTH_SHORT).show();
                                // Reiniciar el formulario

                            } else {
                                Toast.makeText(ActivityListadoBebidas.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityListadoBebidas.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //String idfactura ="1";
                params.put("idproducto", String.valueOf(idproducto));
                //params.put("idfactura", idfactura);
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
