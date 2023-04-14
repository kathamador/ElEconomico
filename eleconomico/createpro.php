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
$idproducto = $_POST["idproducto"];

$sql_last_idfactura = "SELECT idfactura FROM factura ORDER BY idfactura DESC LIMIT 1";
$result_last_idfactura = $conn->query($sql_last_idfactura);

if ($result_last_idfactura->num_rows > 0) {
    $row = $result_last_idfactura->fetch_assoc();
    $idfactura = $row["idfactura"];
} else {
    // Si no hay registros en la tabla factura, se asigna un valor por defecto
    $idfactura = 1;
}

$cantidad = $_POST["cantidad"];
$subtotal = $_POST["subtotal"];


// Insertar los datos en la tabla de la base de datos
$sql = "INSERT INTO detalle_factura (idproducto, idfactura, cantidad, subtotal) VALUES ('$idproducto', '$idfactura', '$cantidad','$subtotal')";
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