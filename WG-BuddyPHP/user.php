<?php
header('Content-type: application/json');
include "Utilities.php";

$table = "wgb_user";
$name = "User";

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
	else if (isset($_GET["changeKey"]))
	{
		$_GET["changeKey"] = md5($_GET["changeKey"]);
		echo Utilities::getData($_GET, $table, $name);
	}
	else
	{
		echo Utilities::getData($_GET, $table, $name);
	}
}
?>