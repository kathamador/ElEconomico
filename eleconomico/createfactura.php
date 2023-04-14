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
$idfactura = $_POST["idfactura"];
$num_comprobante = $_POST["num_comprobante"];
$fecha = $_POST["fecha"];
$idagentecliente = $_POST["idagentecliente"];


// Insertar los datos en la tabla de la base de datos
$sql = "INSERT INTO factura (idfactura, num_comprobante, fecha, idagentecliente) VALUES ('$idfactura', '$num_comprobante', '$fecha','$idagentecliente')";
if ($conn->query($sql) === TRUE) {
    $response["success"] = 1;
    $response["message"] = "Datos guardados correctamente";
} else {
    $response["success"] = 0;
    $response["message"] = "Error al guardar los datos:" . $conn->error;
}

// Salida como JSON
header('Content-Type: application/json');
echo json_encode($response);

// Cierre de la conexión a la base de datos
$conn->close();

?>