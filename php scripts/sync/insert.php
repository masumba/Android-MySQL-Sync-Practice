<?php
	$userName = "root";
	$password = "";
	$host = "localhost";
	$databaseName = "android_sync";
	$con = mysqli_connect($host, $userName, $password, $databaseName);
	if( $con ) {
		$firstName = $_POST["first_name"];
		$lastName = $_POST["last_name"];
		$insertQuery = "INSERT INTO persons (first_name, last_name) VALUES ('".$firstName."', '".$lastName."');";
		$queryResult = mysqli_query($con, $insertQuery);
		if($queryResult) {
			$status = "ok";
		} else {
			$status = "Failed";
		}
	} else {
		$status = "Failed";
	}
	echo json_encode(array("response"=>$status));
	mysqli_close($con);