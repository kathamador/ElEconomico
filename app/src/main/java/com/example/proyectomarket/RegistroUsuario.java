package com.example.proyectomarket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectomarket.validatedlogin.JavaMailAPI;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;


public class RegistroUsuario extends AppCompatActivity {
    Button btnLogin,btnRegistrar;
    int randomNumber;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Drawable image;
    Bitmap bitmap;
    private EditText email, subject, message;
    private EditText editTextNombre, editTextApellido, editTextTelefono, editTextCorreo, editTextContraseña,textdescrip;
    String nombre, apellido, correov, telefono, contrasena,descripcion;
    Intent intent;
    TextView tvlatitud, tvlongitud;
    public static String latitud = "";
    public static String longitud = "";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        ImageButton btnVerPassword = findViewById(R.id.btnVerPass);
        EditText txtPass = findViewById(R.id.txtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        email = findViewById(R.id.txtCorreo);
        editTextNombre = findViewById(R.id.txtNombre);
        editTextApellido = findViewById(R.id.txtApellido);
        editTextTelefono = findViewById(R.id.txtTelefono);
        editTextCorreo = findViewById(R.id.txtCorreo);
        editTextContraseña = findViewById(R.id.txtPass);
        textdescrip = findViewById(R.id.txtdescripcion);
        tvlatitud = findViewById(R.id.txtlatitud);
        tvlongitud = findViewById(R.id.txtlongitud);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroUsuario.this, Login.class);
                startActivity(intent);
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 nombre = editTextNombre.getText().toString();
                 apellido = editTextApellido.getText().toString();
                 telefono = editTextTelefono.getText().toString();
                 correov = editTextCorreo.getText().toString().trim();
                 contrasena = editTextContraseña.getText().toString();
                 descripcion = textdescrip.getText().toString();
                // Validar que los campos no estén vacíos
                if (TextUtils.isEmpty(nombre)) {
                    editTextNombre.setError("Por favor ingrese su nombre");
                    return;
                }
                if (TextUtils.isEmpty(apellido)) {
                    editTextApellido.setError("Por favor ingrese su apellido");
                    return;
                }
                if (TextUtils.isEmpty(telefono)) {
                    editTextTelefono.setError("Por favor ingrese su número de teléfono");
                    return;
                }
                if (TextUtils.isEmpty(correov)) {
                    editTextCorreo.setError("Por favor ingrese su correo electrónico");
                    return;
                }
                if (TextUtils.isEmpty(contrasena)) {
                    editTextContraseña.setError("Por favor ingrese su contraseña");
                    return;
                }
                if (TextUtils.isEmpty(descripcion)) {
                    textdescrip.setError("Por favor ingrese su descripcion");
                    return;
                }
                // validar si el corro tiene un formato correcto
                if (!EMAIL_PATTERN.matcher(correov).matches()) {
                    editTextCorreo.setError("La dirección de correo electrónico no es válida");
                    return;
                }
                Random random = new Random();
                randomNumber = random.nextInt(9000) + 1000;
                sendEmail();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroUsuario.this);
                builder.setTitle("Confirmación");
                builder.setMessage("Ingresa el código de confirmación enviado a su correo:");

                // Agregar un EditText para que el usuario ingrese el código
                final EditText input = new EditText(RegistroUsuario.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Agregar los botones "Aceptar" y "Cancelar"
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String code = input.getText().toString();
                        if (code.equals(String.valueOf(randomNumber))) {
                            // El código es correcto, realizar envío de datos
                            validarCorreo(correov);
                            editTextNombre.setText("");
                            editTextApellido.setText("");
                            editTextTelefono.setText("");
                            editTextCorreo.setText("");
                            editTextContraseña.setText("");
                            textdescrip.setText("");
                        } else {
                            // El código es incorrecto, mostrar mensaje de error
                            Toast.makeText(RegistroUsuario.this, "Código incorrecto", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                // Mostrar la ventana de confirmación
                builder.show();
            }
        });
    }
    ///ubicacion
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setRActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Localizacion */
    public class Localizacion implements LocationListener {
        RegistroUsuario registro;

        public RegistroUsuario getMainActivity() {
            return registro;
        }

        public void setRActivity(RegistroUsuario mainActivity) {
            this.registro = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            RegistroUsuario.setLatitud(loc.getLatitude()+"");
            RegistroUsuario.setLongitud(loc.getLongitude()+"");
            tvlatitud.setText(loc.getLatitude()+"");
            tvlongitud.setText(loc.getLongitude()+"");
            this.registro.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    public static void setLatitud(String latitud) {
        RegistroUsuario.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        RegistroUsuario.longitud = longitud;
    }
    private void sendEmail() {
            String mEmail = email.getText().toString().trim();
            String mSubject = "Codigo de verificación de cuenta";
            // Generar número aleatorio de 4 cifras
            String mMessage = String.valueOf(randomNumber);
            JavaMailAPI javaMailAPI = new JavaMailAPI(this, mEmail, mSubject, mMessage);
            javaMailAPI.execute();
        }
        private void guardarDatos(String nombre, String apellido, String telefono, String correo, String contraseña,String descripcion) {

        String url = "https://finalproyect.com/eleconomico/Cliente/saveCliente.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int successInt = jsonResponse.getInt("success");
                            boolean success = (successInt == 1);
                            String message = jsonResponse.getString("message");
                            if (success) {
                                Toast.makeText(RegistroUsuario.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegistroUsuario.this, message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroUsuario.this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido", apellido);
                params.put("telefono", telefono);
                params.put("correo", correo);
                params.put("contraseña", contraseña);
                params.put("descripcion", descripcion);
                params.put("latitud", latitud);
                params.put("longitud", longitud);
                //params.put("foto",imagenBase64);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void validarCorreo(String correo) {
        String url = "https://finalproyect.com/eleconomico/Cliente/validationCliente.php";

        // Hacemos la petición POST al servidor
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Convertimos la respuesta a JSON
                            JSONObject jsonObject = new JSONObject(response);

                            // Obtenemos el valor de la clave "success"
                            int success = jsonObject.getInt("success");

                            // Si success es 1, el correo existe en la base de datos
                            if (success == 1) {
                                Toast.makeText(getApplicationContext(), "El correo ya está registrado", Toast.LENGTH_LONG).show();
                            } else {
                                guardarDatos(nombre, apellido, telefono, correov, contrasena,descripcion);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros que se envían al servidor
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo);
                return params;
            }
        };
        // Añadimos la petición a la cola de peticiones
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void informacioncliente(){
        Intent intent = new Intent(getApplicationContext(), ActivityListadoBebidas.class);
        intent.putExtra("nombre",nombre);
        startActivity(intent);
    }

    public void datosCliente(){
       // Intent inte = new Intent(getApplicationContext(), ActivityMapa.class);
        Intent i = new Intent(getApplicationContext(), ActivityActualizarDatos.class);
        intent.putExtra("nombre",nombre);
        intent.putExtra("latitud",latitud);
        intent.putExtra("longitud",longitud);

        i.putExtra("nombre",nombre);
        i.putExtra("latitud",latitud);
        i.putExtra("longitud",longitud);

        startActivity(intent);
        startActivity(i);
    }
    //metodos de imagen

}