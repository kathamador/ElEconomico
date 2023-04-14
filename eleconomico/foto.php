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

// Obtener los datos enviados desde Android Studio
$idagentecliente = $_GET["idagentecliente"];
$foto_base64 = $_GET["foto"];

// Convertir la foto en base64 a una imagen
$foto = imagecreatefromstring(base64_decode($foto_base64));
$image = imagecreatefromstring($foto);

// Convertir la imagen a una cadena base64
ob_start();
imagejpeg($image);
$foto_base64 = ob_get_clean();

// Actualizar la foto en la base de datos
$sql = "UPDATE agente_cliente SET foto = ? WHERE idagentecliente = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $foto_base64, $idagentecliente);
header('Content-type: application/json');

if ($stmt->execute() === TRUE) {
    $response["estado"] = TRUE;
    $response["mensaje"] = "Foto actualizada con éxito";
} else {
    $response["estado"] = FALSE;
    $response["mensaje"] = "Error al actualizar la foto: " . $conn->error;
}

echo json_encode($response);
$conn->close();

?>