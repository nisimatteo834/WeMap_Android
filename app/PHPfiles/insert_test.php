<?php

if($_SERVER["REQUEST_METHOD"]=="POST"){
	require 'connection.php';
	createItem();
}
function createItem()
{
	global $connect;
	
	$id = $_POST["id"];	
	$name = $_POST["name"];
	$quantity = $_POST["quantity"];
	
	$query = " Insert into inventory(id,name,quantity) values ('$id','$name','$quantity');";
	
	mysqli_query($connect, $query) or die (mysqli_error($connect));
	mysqli_close($connect);
	
}
?>