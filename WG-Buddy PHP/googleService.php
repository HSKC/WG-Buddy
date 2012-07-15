<?php
header('Content-type: application/json');
include "Utilities.php";

/**
 * Google Authenticate-Schlüssel holen und in Datenbank speichern.
 * @param Variable $username Google-Username
 * @param unknown_type $password Google-Passwort
 * @param unknown_type $source Company-AppName-Version
 * @param unknown_type $service Service "ac2dm"
 */
function googleAuthenticate($username, $password, $source="Company-AppName-Version", $service="ac2dm")
{
	// get an authorization token
	$ch = curl_init();
	if(!ch)
	{
		return false;
	}

	curl_setopt($ch, CURLOPT_URL, "https://www.google.com/accounts/ClientLogin");
	$post_fields = "accountType=" . urlencode('HOSTED_OR_GOOGLE')
	. "&Email=" . urlencode($username)
	. "&Passwd=" . urlencode($password)
	. "&source=" . urlencode($source)
	. "&service=" . urlencode($service);
	curl_setopt($ch, CURLOPT_HEADER, true);
	curl_setopt($ch, CURLOPT_POST, true);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $post_fields);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FRESH_CONNECT, true);
	curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

	// for debugging the request
	//curl_setopt($ch, CURLINFO_HEADER_OUT, true); // for debugging the request

	$response = curl_exec($ch);

	//var_dump(curl_getinfo($ch)); //for debugging the request
	//var_dump($response);

	curl_close($ch);

	if (strpos($response, '200 OK') === false) {
		return false;
	}

	// find the auth code
	preg_match("/(Auth=)([\w|-]+)/", $response, $matches);

	if (!$matches[2]) {
		return false;
	}
	 
	// Code in Datenbank speichern.
	$objConn = Utilities::getConnection();
	$query = "UPDATE wgb_androidToken SET authCode='" . $matches[2] ."'";
	$objConn->query($query);

	return $matches[2];
}


/**
 * Nachricht an alle Geräte einer WG schicken.
 * @param $wgId: WG-ID
 * @param $msgType: all messages with same type may be "collapsed": if multiple are sent,
 * @param $messageText Nachrichtentext
 */
function sendMessageToPhone($wgId, $msgType, $messageText)
{
	$objConn = Utilities::getConnection();
	$query = "SELECT * FROM wgb_androidToken";
	$objResult = $objConn->query($query);
	$array = Utilities::getResultsArray($objResult);
	$authCode = $array[0]['authCode'];

	$query = "SELECT * FROM wgb_user WHERE wgId='" . $wgId . "'";
	$objResult = $objConn->query($query);
	$array = Utilities::getResultsArray($objResult);

	// Alle Geräte durchlaufen und an jedes Gerät die Nachricht senden.
	foreach($array as $key => $value)
	{
		$headers = array('Authorization: GoogleLogin auth=' . $authCode);
		$data = array(
	    	'registration_id' => $value["android_key"],
	        'collapse_key' => $msgType,
	        'data.message' => $messageText           
		);

		$ch = curl_init();

		curl_setopt($ch, CURLOPT_URL, "https://android.apis.google.com/c2dm/send");
		if ($headers)
		curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
		curl_setopt($ch, CURLOPT_POST, true);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $data);

		curl_exec($ch);
		curl_close($ch);
	}
}

$get_array = $_GET;
$post_arrayarray = $_POST;

// Prüfen, ob Anfrage die Zugangsberechtigung hat.
if(Utilities::checkAuthentication($array))
{
	unset($get_array['authCode']);

	if (isset($array["Authenticate"]))
	{
		echo googleAuthenticate(Config::$google_email, Config::$google_pw);
	}
	else
	{
		sendMessageToPhone($post_array['wgId'], $post_array['msgType'], $post_array['messageText']);
	}
}
?>