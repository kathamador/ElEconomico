<?php
// Conexión a la base de datos
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "eleconomico";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Conexión fallida: " . $conn->connect_error);
}

// Obtener el ID del agente/cliente
$idagentecliente = $_GET['idagentecliente'];

// Obtener la imagen de la base de datos en formato Base64
$sql = "SELECT foto FROM agente_cliente WHERE idagentecliente = '$idagentecliente'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $imagen_base64 = $row['foto'];
    echo $imagen_base64;
} else {
    echo "No se encontró la imagen en la base de datos";
}

$conn->close();
?>
