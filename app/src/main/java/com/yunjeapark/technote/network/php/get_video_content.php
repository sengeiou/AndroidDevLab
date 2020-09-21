<?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $id=isset($_POST['id']) ? $_POST['id'] : '1';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    if ($id != "" ){ 

        $sql="select * from video where id='$id'";
        $stmt = $con->prepare($sql);
        $stmt->execute();
 
        if ($stmt->rowCount() == 0){

            echo "'";
            echo $id;
            echo "'은 찾을 수 없습니다.";
        }
	    else{

   		    $data = array(); 

            while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

        	extract($row);

            array_push($data, array('id'=>$id,
            'video_url'=>$video_url,
		    'title'=>$title,
		    'content'=>$content));
            }

            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("yjpapp"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
    else {
        echo "ID 오류";
    }
?>
