<?php
if($_SERVER["REQUEST_METHOD"]=="GET"){
	include 'connection.php';
	showItem();
}

function showItem()
{
global $connect;
$mysql_qry = "select building,name,floor,campus from allrooms";
$result = mysqli_query($connect,$mysql_qry);

$output=array();
while($row = mysqli_fetch_assoc($result))
  {
	array_push($output,$row);
  }

echo json_encode(array("allrooms"=>$output));

mysqli_close($connect);
}
?>