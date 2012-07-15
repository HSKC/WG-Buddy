<?php
/**
 * Klasse enthält alle vom Server benötigten Einstellungen.
 */
class Config
{
	public static $auth_code = "1234567890";								// Authcode für die sichere Verbindung mit dem Android-Client
	public static $app_link = "url/WG-Buddy.apk";							// Link zur App

	public static $db_host = "localhost";
	public static $db_user = "user";
	public static $db_pw = "pw";
	public static $db_db = "db";

	public static $google_email = "email@googlemail.com";					// Google-Account Username
	public static $google_pw = "Passwort";									// Google-Account Passwort
}
?>