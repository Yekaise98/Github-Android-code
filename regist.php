<?php

$conn=mysqli_connect('localhost','root','','login2');

	if(isset($_POST['user_name']) && isset($_POST['password']))
	{
	$username=$_POST['user_name'];
	$pasword=$_POST['password'];
	
	$qry="SELECT * FROM `loginpage` WHERE username like '$username' AND password like '$pasword'";
	$run=mysqli_query($conn,$qry);
	 $data=mysqli_num_rows($run);
	 if($data<1)
	 {
		echo " no success";
	
	 }
	 else
	 {
		 echo "success";
	 }
	}
 
?>