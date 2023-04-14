<?php

if(isset($_GET['id'])){

    $id_pedido = $_GET['id'];
    $calificacion = $_GET['calificacion'];

    $conn = mysqli_connect('localhost', 'root', '', 'eleconomico');

    if(!$conn){
        die('Error al conectarse a la base de datos: '.mysqli_connect_error());
    }


    $sql = "UPDATE pedidos SET calificacion = $calificacion WHERE idpedidos = $id_pedido";
    if(mysqli_query($conn, $sql)){

        $response['success'] = true;
        $response['message'] = "Estado del pedido actualizado correctamente";
        echo json_encode($response);
    } else {

        $response['success'] = false;
        $response['message'] = "Error al actualizar el estado del pedido";
        echo json_encode($response);
    }


} else {

    $response['success'] = false;
    $response['message'] = "ID del pedido no recibido";
    echo json_encode($response);
}

?>