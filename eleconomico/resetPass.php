<?php

if(isset($_GET['correo'] )){

    $correo = $_GET['correo'];
    $password = $_GET['password'];

    $conn = mysqli_connect('localhost', 'root', '', 'eleconomico');

    if(!$conn){
        die('Error al conectarse a la base de datos: '.mysqli_connect_error());
    }


    $sql = "UPDATE agente_cliente SET contraseña = '$password' WHERE correo = '$correo'";
    if(mysqli_query($conn, $sql)){

        $response['success'] = true;
        $response['message'] = "Contraseña temporal actualizada";
        echo json_encode($response);
    } else {

        $response['success'] = false;
        $response['message'] = "Error al actualizadar contraseña temporal ";
        echo json_encode($response);
    }


} else {

    $response['success'] = false;
    $response['message'] = "No se recibió un valor";
    echo json_encode($response);
}

?>