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

// Obtener el correo enviado desde la aplicación
$correo = $_POST["correo"];

// Verificar si el correo ya existe en la base de datos
$sql = "SELECT correo FROM agente_cliente WHERE correo='$correo'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // El correo ya existe
    $response["success"] = 1;
    $response["message"] = "El correo ya existe en la base de datos.";
} else {
    // El correo no existe
    $response["success"] = 0;
    $response["message"] = "El correo no existe en la base de datos.";
}

// Salida como JSON
header('Content-Type: application/json');
echo json_encode($response);

// Cierre de la conexión a la base de datos
$conn->close();

?>
