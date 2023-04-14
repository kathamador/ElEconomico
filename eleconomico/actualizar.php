<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: *");
$conexion = new mysqli('localhost', 'root', '', 'eleconomico');

$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

$id = $request->idagentecliente;

$numero = $request->telefono;
$contra = $request->contraseña;
$descri = $request->descripcion;
$latitud = $request->latitud;
$longitud = $request->longitud;

$sql_query = "UPDATE agente_cliente SET telefono='$numero', contraseña='$contra',descripcion='$descri', latitud='$latitud', longitud='$longitud' WHERE idagentecliente='$id'";

$result = $conexion->query($sql_query);

if ($result) {
    $resultado = array('estado' => true);
    echo json_encode($resultado);
} else {
    $resultado = array('estado' => false);
    echo json_encode($resultado);
}
?>