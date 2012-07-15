<?php
header('Content-type: application/json');
include "Utilities.php";

$table = "wgb_user";
$name = "Mail";

/**
 * Zufällig generierten Schlüssel mit der übergebenen Länge erzeugen.
 * @param Variable $length Länge des Schlüssels.
 */
function generateKey($length)
{
	$key = "";
	for($i = 0; $i < $length; $i++)
	{
		$type = rand(0, 2);
		switch($type)
		{
			case 0:
				$key .= chr(rand(48, 57)); // 0-9
				break;
			case 1:
				$key .= chr(rand(65, 90)); // A-Z
				break;
			case 2:
				$key .= chr(rand(97, 122)); // a-z
				break;
			case 3:
				$key .= chr(rand(33, 44)); // !"#$%&'()*+,
				break;
		}
	}

	return $key;
}

/**
 * E-Mail Header für HTML E-Mails erzeigen.
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 */
function getHeader($username, $email)
{
	// für HTML-E-Mails muss der 'Content-type'-Header gesetzt werden
	$header  = 'MIME-Version: 1.0' . "\r\n";
	$header .= 'Content-type: text/html; charset=utf-8' . "\r\n";

	// zusätzliche Header
	$header .= "From: WG-Buddy <wg-buddy@no-email.com>\r\n";

	return $header;
}

/**
 * E-Mail für "Passwort vergessen".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $key Schlüssel
 */
function lostPasswordMail($username, $email, $key)
{
	$subject = "WG-Buddy: Passwort vergessen";

	$message = "Guten Tag $username,
	
	Mit Hilfe dieses Änderungsschlüssels können Sie ihr WG-Buddy Passwort ändern:
	
	$key
	
	Sollten Sie kein neues Passwort angefordert haben, können Sie diese E-Mail ignorieren und löschen.
	
	PS: Dies ist eine autom. generierte Nachricht. Sie brauchen darauf nicht zu antworten.
		
	Freundliche Grüße
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail für "Konto aktivieren".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $key Schlüssel
 */
function activateAccountMail($username, $email, $key)
{
	$subject = "WG-Buddy: Aktivierung des Kontos";

	$message = "Guten Tag $username,
	
	Mit Hilfe dieses Aktivierungsschlüssels können Sie ihr WG-Buddy Konto aktivieren:
	
	$key
	
	Freundliche Grüße
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail für "Neue Aufgabe".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $taskname Aufgabenname
 * @param Variable $tasktext Aufgabentext
 */
function newTaskMail($username, $email, $taskname, $tasktext)
{
	$subject = "WG-Buddy: Neue Aufgabe";

	$message = "Guten Tag $username,
	
	Sie wurden für folgende Aufgabe ausgwählt:
	
	$taskname
	
	Beschreibung:
	
	$taskname
	
	Freundliche Grüße
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail für "Einladung".
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $username Benutzername
 * @param Variable $wg WG-Name
 * @param Variable $wg WG-Passwort
 */
function sendMailInvite($email, $username, $wg, $pw)
{
	$subject = "WG-Buddy: Einladung";

	$link = Config::$app_link;

	$message = "Guten Tag $username,
	
	Sie wurden von $username eingeladen die WG-Buddy App zu benutzen.
	
	Laden Sie die App einfach unter dem Link $link herunter und treten Sie anschließend der WG $wg bei.
	
	Das Passwort lautet:

	$pw
	
	Freundliche Grüße
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * Parameter für die E-Mails "Passwort vergessen" und "Konto aktivieren" einlesen und entsprechende Funktion aufrufen.
 * @param Variable $parameter Übergebene Parameter.
 */
function sendMail($parameter)
{
	// Daten aus der Datenbank lesen.
	$query = "SELECT * FROM wgb_user WHERE email='" . $parameter . "'";
	$objConn = Utilities::getConnection();
	$objResult = $objConn->query($query);
	$array = Utilities::getResultsArray($objResult);

	$id = $array[0]["id"];
	$username = $array[0]["username"];
	$email = $array[0]["email"];
	$key = generateKey(10); // Schlüssel erzeugen
	$changeKey = md5($key); // Hashfunktion md5() auf Schlüssel ausführen

	$query = "UPDATE wgb_user SET changeKey = '" . $changeKey . "' WHERE id=" . $id;
	$objConn->query($query);

	if (isset($array["lost"]))
	{
		lostPasswordMail($username, $email, $key);
	}
	else
	{
		activateAccountMail($username, $email, $key);
	}
}

/**
 * Parameter für die E-Mail "Neue Aufgabe" aus Array einlesen.
 * @param Variable $array Übergebene Daten.
 */
function sendMailTask($array)
{
	$username = $array["username"];
	$email = $array["email"];
	$taskname = $array["taskname"];
	$tasktext = $array["tasktext"];

	newTaskMail($username, $email, $taskname, $tasktext);
}

// Prüfen, ob Anfrage die Zugangsberechtigung hat.
if(Utilities::checkAuthentication($_GET))
{
	unset($_GET['authCode']);

	if (isset($_GET["lost"]))
	{
		sendMail($_GET["lost"]);
	}
	elseif(isset($_GET["task"]))
	{
		sendMailTask($_POST);
	}
	elseif(isset($_GET["invite"]))
	{
		sendMailInvite($_GET["invite"], $_GET["username"], $_GET["wg"], $_GET["pw"]);
	}
	else
	{
		sendMail($_GET["activate"]);
	}
}
?>

