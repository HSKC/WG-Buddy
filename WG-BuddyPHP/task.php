<?php
header('Content-type: application/json'); 
include "Utilities.php";

$table = "wgb_task";
$name = "Task";

// Prüfen, ob Anfrage die Zugangsberechtigung hat.
if(Utilities::checkAuthentication($_GET))
{
	unset($_GET['authCode']);

	if (isset($_GET["insert"]))
	{
		Utilities::insert($table);
	}
	else if (isset($_GET["update"]))
	{
		Utilities::update($table, $_GET["update"], $_POST);
	}
	else if (isset($_GET["delete"]))
	{
		Utilities::delete($table, $_GET["delete"]);
	}
	else
	{
		echo Utilities::getData($_GET, $table, $name);
	}
}
?>