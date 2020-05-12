<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $sql = 'SELECT * FROM photos ORDER BY id DESC LIMIT :offset, 10;';
    $stmt = $con->prepare($sql);
    $stmt->bindValue(':offset',(int) trim($_GET['offset']),PDO::PARAM_INT);        
    $stmt->execute();

    if ($stmt->rowCount() > 0)
    {
        $data = array();    

        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
	        extract($row);
	        array_push($data, array(
                'id'=>$id,
	            'photo_name'=>$photo_name,
	            'photo_url_1'=>$photo_url_1,
	            'photo_url_2'=>$photo_url_2,
	            'photo_url_3'=>$photo_url_3,
	            'photo_url_4'=>$photo_url_4,
	            'photo_url_5'=>$photo_url_5,	
	            'title'=>$title,
	            'price'=>$price,
	            'subject'=>$subject,
	            'content'=>$content));
        }
        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("yjpapp"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
    }

    function remove_utf8_bom($text){
    $bom = pack('H*','EFBBBF');
    $text = preg_replace("/^$bom/", '', $text);
    return $text;
    }
    echo "query"
?>