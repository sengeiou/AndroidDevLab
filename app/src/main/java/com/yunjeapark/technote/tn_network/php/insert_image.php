<?php
 
require_once 'db_connection.php';
 
global $connection;
$upload_path = 'upload_image/'; //this is our upload folder
$server_ip = gethostbyname(gethostname()); //Getting the server ip
$upload_url = 'http://yjpapp.com/'.$upload_path; //upload url
$image_count = 0;
$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
//response array
$response = array();

if($_SERVER['REQUEST_METHOD']=='POST'){
 	
    //checking the required parameters from the request
    if(isset($_POST['title']))
    {
        
        $title = $_POST['title'];
	$price = $_POST['price'];
	$subject = $_POST['subject'];
	$content = $_POST['content'];

        $fileinfo = pathinfo($_FILES['image']['name']);//getting file info from the request
        $extension = $fileinfo['extension']; //getting the file extension
        $file_url = $upload_url . getFileName() . '.' . $extension; //file url to store in the database
        $file_path = $upload_path . getFileName() . '.'. $extension; //file path to upload in the server
        $img_name = getFileName() . '.'. $extension; //file name to store in the database

        $fileinfo2 = pathinfo($_FILES['image2']['name']);//getting file info from the request
        $extension2 = $fileinfo2['extension2']; //getting the file extension
        $file_url2 = $upload_url . getFileName2() . '.' . $extension2; //file url to store in the database
        $file_path2 = $upload_path . getFileName2() . '.'. $extension2; //file path to upload in the server
        $img_name2 = getFileName2() . '.'. $extension2; //file name to store in the database

	$fileinfo3 = pathinfo($_FILES['image3']['name']);//getting file info from the request
        $extension3 = $fileinfo3['extension3']; //getting the file extension
        $file_url3 = $upload_url . getFileName3() . '.' . $extension3; //file url to store in the database
        $file_path3 = $upload_path . getFileName3() . '.'. $extension3; //file path to upload in the server
        $img_name3 = getFileName3() . '.'. $extension3; //file name to store in the database

	$fileinfo4 = pathinfo($_FILES['image4']['name']);//getting file info from the request
        $extension4 = $fileinfo4['extension4']; //getting the file extension
        $file_url4 = $upload_url . getFileName4() . '.' . $extension4; //file url to store in the database
        $file_path4 = $upload_path . getFileName4() . '.'. $extension4; //file path to upload in the server
        $img_name4 = getFileName4() . '.'. $extension4; //file name to store in the database

	$fileinfo5 = pathinfo($_FILES['image5']['name']);//getting file info from the request
        $extension5 = $fileinfo5['extension5']; //getting the file extension
        $file_url5 = $upload_url . getFileName5() . '.' . $extension5; //file url to store in the database
        $file_path5 = $upload_path . getFileName5() . '.'. $extension5; //file path to upload in the server
        $img_name5 = getFileName5() . '.'. $extension5; //file name to store in the database
 	
	json_encode($title, JSON_UNESCAPED_UNICODE);

        try{
            
           
            //adding the path and name to database
	    if($_POST['image_count']=='1'){
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;
		$sql = "INSERT INTO photos(photo_name, photo_url_1, title, price, subject, content) ";
		$sql .= "VALUES ('{$img_name}', '{$file_url}', '{$title}','{$price}', '{$subject}', '{$content}');";
            }elseif($_POST['image_count']=='2'){
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image2']['tmp_name'],$file_path2); //saving the file to the uploads folder;
		$sql = "INSERT INTO photos(photo_name, photo_url_1, photo_url_2, title, price, subject, content) ";
            	$sql .= "VALUES ('{$img_name}', '{$file_url}', '{$file_url2}', '{$title}','{$price}', '{$subject}', '{$content}');";
	    }elseif($_POST['image_count']=='3'){
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image2']['tmp_name'],$file_path2); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image3']['tmp_name'],$file_path3); //saving the file to the uploads folder;
		$sql = "INSERT INTO photos(photo_name, photo_url_1, photo_url_2, photo_url_3, title, price, subject, content) ";
            	$sql .= "VALUES ('{$img_name}', '{$file_url}', '{$file_url2}', '{$file_url3}', '{$title}','{$price}', '{$subject}', '{$content}');";
	    }elseif($_POST['image_count']=='4'){
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path); // 업로드한 이미지 파일을 저장시킴
	    	move_uploaded_file($_FILES['image2']['tmp_name'],$file_path2); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image3']['tmp_name'],$file_path3); //saving the file to the uploads folder;
		move_uploaded_file($_FILES['image4']['tmp_name'],$file_path4); //saving the file to the uploads folder;
		$sql = "INSERT INTO photos(photo_name, photo_url_1, photo_url_2, photo_url_3, photo_url_4, title, price, subject, content) ";
            	$sql .= "VALUES ('{$img_name}', '{$file_url}', '{$file_url2}', '{$file_url3}', '{$file_url4}', '{$title}','{$price}', '{$subject}', '{$content}');";
	    }elseif($_POST['image_count']=='5'){
		move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image2']['tmp_name'],$file_path2); //saving the file to the uploads folder;
	    	move_uploaded_file($_FILES['image3']['tmp_name'],$file_path3); //saving the file to the uploads folder;
		move_uploaded_file($_FILES['image4']['tmp_name'],$file_path4);
		move_uploaded_file($_FILES['image5']['tmp_name'],$file_path5);
		$sql = "INSERT INTO photos(photo_name, photo_url_1, photo_url_2, photo_url_3, photo_url_4, photo_url_5, title, price, subject, content) ";
            	$sql .= "VALUES ('{$img_name}', '{$file_url}', '{$file_url2}', '{$file_url3}', '{$file_url4}', '{$file_url5}', '{$title}','{$price}', '{$subject}', '{$content}');";
	    }

            if(mysqli_query($connection,$sql)){
                //filling response array with values
                $response['error'] = false;
                $response['photo_name'] = $img_name;
                $response['photo_url_1'] = $file_url;
                $response['photo_url_2'] = $file_url2;
		$response['photo_url_3'] = $file_url3;
		$response['photo_url_4'] = $file_url4;
		$response['photo_url_5'] = $file_url5;
                $response['title'] = $title;
		$response['subject'] = $subject;
		$response['content'] = $content;
		$response['price'] = $price;
		
            }
            //if some error occurred
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        }
        //displaying the response
        echo json_encode($response);
 
        //closing the connection
        mysqli_close($connection);
    }else{
        $response['error'] = true;
        $response['message']='Please choose a file';
    }
}
 
/*
We are generating the file name
so this method will return a file name for the image to be uploaded
*/
function getFileName(){
    global $connection;
     
    $sql = "SELECT max(id) as id FROM photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));
 	
    if($result['id']== null)
        return 1;
    else{
	++$result['id'];
	return "{$result['id']}_1";    
    }
     
    mysqli_close($connection);
}

function getFileName2(){
    global $connection;
     
    $sql = "SELECT max(id) as id FROM photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));
 
    if($result['id']== null)
        return 1;
    else{
	++$result['id'];
	return "{$result['id']}_2";    
    }

     
    mysqli_close($connection);
}
function getFileName3(){
    global $connection;
     
    $sql = "SELECT max(id) as id FROM photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));
 
    if($result['id']== null)
        return 1;
    else{
	++$result['id'];
	return "{$result['id']}_3";    
    }
     
    mysqli_close($connection);
}
function getFileName4(){
    global $connection;
     
    $sql = "SELECT max(id) as id FROM photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));
 
    if($result['id']== null)
        return 1;
    else{
	++$result['id'];
	return "{$result['id']}_4";    
    }
     
    mysqli_close($connection);
}
function getFileName5(){
    global $connection;
     
    $sql = "SELECT max(id) as id FROM photos";
    $result = mysqli_fetch_array(mysqli_query($connection, $sql));
 
    if($result['id']== null)
        return 1;
    else{
	++$result['id'];
	return "{$result['id']}_5";    
    }
     
    mysqli_close($connection);
}
?>