<?php
    /**
    *Database config variables,
    */
    define("DB_HOST","localhost"); 
    define("DB_USER","dbswoqkr");
    define("DB_PASSWORD","fnclrkakql3#");
    define("DB_DATABASE","dbswoqkr");
	
    $options = array(PDO::MYSQL_ATTR_INIT_COMMAND => 'SET NAMES utf8'); 	
   
    $connection = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
    
    $mysqli = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
    mysqli_query($mysqli, "set names utf8");

    if(mysqli_connect_errno()){
        die("Database connnection failed " . "(" .
            mysqli_connect_error() . " - " . mysqli_connect_errno() . ")"
                );
    } 
    header('Content-Type: text/html; charset=utf-8'); 		
?>