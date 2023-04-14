<?php

// Conexión a la base de datos
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "eleconomico";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Obtener los datos enviados desde la aplicación
$nombre = $_POST["nombre"];
$apellido = $_POST["apellido"];
$telefono = $_POST["telefono"];
$correo = $_POST["correo"];
$contrasena = $_POST["contraseña"];
$descripcion = $_POST["descripcion"];
$latitud = $_POST["latitud"];
$longitud = $_POST["longitud"];

// Insertar los datos en la base de datos
$sql = "INSERT INTO agente_cliente (nombre, apellido, telefono, correo, contraseña,descripcion, latitud, longitud) VALUES ('$nombre', '$apellido', '$telefono', '$correo', '$contrasena','$descripcion','$latitud','$longitud')";

if ($conn->query($sql) === TRUE) {
    $response["success"] = 1;
    $response["message"] = "Datos guardados correctamente.";
} else {
    $response["success"] = 0;
    $response["message"] = "Error al guardar los datos: " . $conn->error;
}

// Salida como JSON
header('Content-Type: application/json');
echo json_encode($response);

// Cierre de la conexión a la base de datos
$conn->close();

?>
