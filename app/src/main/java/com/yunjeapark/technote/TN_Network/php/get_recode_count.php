<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1);

    $conn = mysqli_connect("localhost", "dbswoqkr", "fnclrkakql3#", "dbswoqkr");

    $sql = 'SELECT * FROM photos ';

    $result_set = mysqli_query($conn, $sql);

    $data = array(); 
    $totla_recode = mysqli_num_rows($result_set);

    array_push($data, array('recode'=>$totla_recode));

    $json = json_encode(array("yjpapp"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
    
    echo $json;

    mysqli_close($conn);

?>