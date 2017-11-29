<?php
if($_SERVER["REQUEST_METHOD"]=="GET"){
	include 'connection.php';
	showItem();
}

function showItem()
{
	global $connect;
//mysql_select_db($db_name, $conn);
$mysql_qry = "select * from inventory";
$result = mysqli_query($connect,$mysql_qry);

$output=array();
while($row = mysqli_fetch_assoc($result))
  {
	array_push($output,$row);
	//$output[]=$row;
  }

//print(json_encode($output));
echo json_encode(array("inventory"=>$output));

mysqli_close($connect);
}
?>