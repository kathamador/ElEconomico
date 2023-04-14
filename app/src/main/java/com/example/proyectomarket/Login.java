package com.example.proyectomarket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectomarket.validatedlogin.JavaMailAPI;
import com.example.proyectomarket.validatedlogin.ValidarCredenciales;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Login extends AppCompatActivity {
    Button btnRegistroUsuario,btniniciar;
    EditText editTextCorreo,editTextContrasena;
    TextView txtResetPass;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegistroUsuario = (Button) findViewById(R.id.btnRegistrar);
        btniniciar= (Button) findViewById(R.id.btnIniciarSesion);
        ImageButton btnVerPassword =  findViewById(R.id.btnVerPass);
        EditText txtPass = findViewById(R.id.txtPass);
        editTextCorreo = findViewById(R.id.txtUser);
        editTextContrasena = findViewById(R.id.txtPass);
        txtResetPass = findViewById(R.id.txtResetPass);

        btnRegistroUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegistroUsuario.class);
                startActivity(intent);
            }
        });

        btniniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = editTextCorreo.getText().toString();
                String contrasena = editTextContrasena.getText().toString();
                if (TextUtils.isEmpty(correo)) {
                    editTextCorreo.setError("Por favor ingrese su correo");
                    return;
                }
                if (TextUtils.isEmpty(contrasena)) {
                    editTextContrasena.setError("Por favor ingrese su contraseña");
                    return;
                }
                // Validar las credenciales en segundo plano
                ValidarCredenciales validarCredenciales = new ValidarCredenciales(getApplicationContext());
                validarCredenciales.execute(correo, contrasena);
            }
        });
        btnVerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cursorPosition = txtPass.getSelectionStart(); // Guarda la posición del cursor

                if (txtPass.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    // Si el EditText tiene el texto oculto, muestra los caracteres
                    txtPass.setTransformationMethod(null);
                } else {
                    // Si el EditText tiene los caracteres visibles, oculta el texto
                    txtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                txtPass.setSelection(cursorPosition); // Restaura la posición del cursor
            }
        });
        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextCorreo.getText())) {
                    editTextCorreo.setError("Por favor ingrese su correo");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage("¿Desea enviar una nueva contraseña a su correo?");
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });
    }
    private void sendEmail() {

        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            randomString.append(randomChar);
        }

        String mEmail = editTextCorreo.getText().toString().trim();
        String mSubject = "Usted ha solicitado una nueva contraseña, su contraseña es temporal, por favor cambiarla luego de iniciar sesión por su seguridad";

        String mMessage = randomString.toString();

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();
        mandarDatos(mMessage, mEmail);
    }
    public void mandarDatos( String NewPassword, String Correo) {
        mQueue = Volley.newRequestQueue(this);
        String url = "https://finalproyect.com/eleconomico/resetPass.php?password="+NewPassword+"&correo="+Correo;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        // actualizar tabla pedidos aquí
                        Toast.makeText(Login.this, "Su contraseña ha sido enviada con éxito", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Login.this, "Su contraseña no se ha enviado", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error.-", "error de servidor");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error.-", "error de conexión ");
            }
        });

        mQueue.add(jsonObjectRequest);
    }
}