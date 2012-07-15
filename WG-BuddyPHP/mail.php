<?php
header('Content-type: application/json');
include "Utilities.php";

$table = "wgb_user";
$name = "Mail";

/**
 * Zuf�llig generierten Schl�ssel mit der �bergebenen L�nge erzeugen.
 * @param Variable $length L�nge des Schl�ssels.
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
 * E-Mail Header f�r HTML E-Mails erzeigen.
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 */
function getHeader($username, $email)
{
	// f�r HTML-E-Mails muss der 'Content-type'-Header gesetzt werden
	$header  = 'MIME-Version: 1.0' . "\r\n";
	$header .= 'Content-type: text/html; charset=utf-8' . "\r\n";

	// zus�tzliche Header
	$header .= "From: WG-Buddy <wg-buddy@no-email.com>\r\n";

	return $header;
}

/**
 * E-Mail f�r "Passwort vergessen".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $key Schl�ssel
 */
function lostPasswordMail($username, $email, $key)
{
	$subject = "WG-Buddy: Passwort vergessen";

	$message = "Guten Tag $username,
	
	Mit Hilfe dieses �nderungsschl�ssels k�nnen Sie ihr WG-Buddy Passwort �ndern:
	
	$key
	
	Sollten Sie kein neues Passwort angefordert haben, k�nnen Sie diese E-Mail ignorieren und l�schen.
	
	PS: Dies ist eine autom. generierte Nachricht. Sie brauchen darauf nicht zu antworten.
		
	Freundliche Gr��e
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail f�r "Konto aktivieren".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $key Schl�ssel
 */
function activateAccountMail($username, $email, $key)
{
	$subject = "WG-Buddy: Aktivierung des Kontos";

	$message = "Guten Tag $username,
	
	Mit Hilfe dieses Aktivierungsschl�ssels k�nnen Sie ihr WG-Buddy Konto aktivieren:
	
	$key
	
	Freundliche Gr��e
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail f�r "Neue Aufgabe".
 * @param Variable $username Benutzername
 * @param Variable $email E-Mail Adresse des Benutzers
 * @param Variable $taskname Aufgabenname
 * @param Variable $tasktext Aufgabentext
 */
function newTaskMail($username, $email, $taskname, $tasktext)
{
	$subject = "WG-Buddy: Neue Aufgabe";

	$message = "Guten Tag $username,
	
	Sie wurden f�r folgende Aufgabe ausgw�hlt:
	
	$taskname
	
	Beschreibung:
	
	$taskname
	
	Freundliche Gr��e
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * E-Mail f�r "Einladung".
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
	
	Laden Sie die App einfach unter dem Link $link herunter und treten Sie anschlie�end der WG $wg bei.
	
	Das Passwort lautet:

	$pw
	
	Freundliche Gr��e
	Ihr WG-Buddy Team";

	mail($email, $subject, $message);
}

/**
 * Parameter f�r die E-Mails "Passwort vergessen" und "Konto aktivieren" einlesen und entsprechende Funktion aufrufen.
 * @param Variable $parameter �bergebene Parameter.
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
	$key = generateKey(10); // Schl�ssel erzeugen
	$changeKey = md5($key); // Hashfunktion md5() auf Schl�ssel ausf�hren

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
 * Parameter f�r die E-Mail "Neue Aufgabe" aus Array einlesen.
 * @param Variable $array �bergebene Daten.
 */
function sendMailTask($array)
{
	$username = $array["username"];
	$email = $array["email"];
	$taskname = $array["taskname"];
	$tasktext = $array["tasktext"];

	newTaskMail($username, $email, $taskname, $tasktext);
}

// Pr�fen, ob Anfrage die Zugangsberechtigung hat.
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

