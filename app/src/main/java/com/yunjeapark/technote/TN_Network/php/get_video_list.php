<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    $stmt = $con->prepare('select * from video');
    $stmt->execute();
	
    if ($stmt->rowCount() > 0)
    {
        $data = array(); 

        while($row=$stmt->fetch(PDO::FETCH_ASSOC))
        {
            extract($row);
    
            array_push($data, 
                array('id'=>$id,
                'video_name'=>$video_name,
                'video_url'=>$video_url,
	    'thumbnail_url'=>$thumbnail_url,
	    'title'=>$title,
	    'content'=>$content
			
            ));
	    
        }
        header('Content-Type: application/json; charset=utf8');
        $json = json_encode(array("yjpapp"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
        echo $json;
		
    }

function remove_utf8_bom($text)
{
    $bom = pack('H*','EFBBBF');
    $text = preg_replace("/^$bom/", '', $text);
    return $text;
}
?>