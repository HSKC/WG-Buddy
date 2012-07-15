<?php
include "Config.php";
/**
 * Stellt alle Methoden zur Datenbank-Interaktion zur Verfügung.
 */
class Utilities
{
	/**
	 * Stellt die Verbindung zur Datenbank her.
	 */
	public static function getConnection()
	{
		$objConn = new mysqli(Config::$db_host, Config::$db_user, Config::$db_pw);
		$objConn->select_db(Config::$db_db);
		return $objConn;
	}

	/**
	 * Beendet die Verbindung zur Datenbank.
	 */
	public static function closeConnection()
	{
		$objConn->close;
	}

	/**
	 * Prüft, ob die Anfrage authorisiert ist.
	 * @param Variable $array Array binhaltet das Feld authCode.
	 */
	public static function checkAuthentication($array)
	{
		if(Config::$auth_code == $array["authCode"])
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Erstellt aus den übergebenen Get-Parametern den SQL-Query-String.
	 * @param Variable $array Enthält die übergebenen Parameter.
	 */
	public static function getParameterFromGet($array)
	{
		$where = "";
		$orderby = "";
		$direction = " DESC";
		if(isset($array))
		{
			foreach ($array as $key => $value)
			{
				if($key == "orderby")
				{
					$orderby = " ORDER BY " . $value;
				}
				else if($key == "direction")
				{
					$direction = " " . $value;
				}
				else
				{
					if($where == "")
					{
						$where .= " WHERE ";
					}
					else
					{
						$where .= " AND ";
					}
					$where .= $key . "=" . ((is_numeric($value))? $value : "'" . $value . "'");
				}
			}
		}
		return $where . $orderby . (($orderby != "")? $direction : "");
	}
	
	/**
	 * Liest Daten aus der Datenbank.
	 * @param Variable $array Enthält die übergebenen Parameter.
	 * @param Variable $table Die Datenbank-Tabelle auf die zugegriffen werden soll.
	 * @param Variable $name Item-Name für das JSON-Protokoll.
	 */
	public static function getData($array, $table, $name)
	{
		$parameter = Utilities::getParameterFromGet($array);
		
		$query = "SELECT * FROM " . $table . $parameter;
		
		$objConn = Utilities::getConnection();
		$objResult = $objConn->query($query);
		
		return Utilities::getResults($objResult, $name);
	}

	/**
	 * Erzeugt aus einem Datenbank-Anfragen Ergebnis einen Array.
	 * @param Variable $objResult Ergebnis der Query-Anfrage. 
	 */
	public static function getResultsArray($objResult)
	{
		$rows = array();
		while($r = mysqli_fetch_assoc($objResult))
		{
			$rows[] = $r;
		}

		return $rows;
	}

	/**
	 * Erzeugt aus einem Datenbank-Anfragen Ergebnis einen JSON-String.
	 * @param Variable $objResult Ergebnis der Query-Anfrage. 
	 * @param Variable $name Item-Name für das JSON-Protokoll.
	 */
	public static function getResults($objResult, $name)
	{
		$rows = array();
		while($r = mysqli_fetch_assoc($objResult))
		{
			$rows[] = array_map('htmlentities',$r);
		}

		return "{ \"" . $name . "\": " . html_entity_decode(json_encode($rows)) . "}";
	}

	/**
	 * Stellt die Verbindung zur Datenbank her.
	 * @param Variable $table Speichert
	 */
	public static function insert($table)
	{
		if (isset($_POST))
		{
			$keys = "";
			$values = "";
			foreach ($_POST as $key => $value)
			{
				if($keys != "")
				{
					$keys .= ", ";
					$values .= ", ";
				}

				$keys .= $key;
				$values .= (is_numeric($value))? $value : "'" . $value . "'";
			}
			$query = "INSERT INTO " . $table . " (" . $keys . ") VALUES (" . $values . ")";
			echo $query;
			$objConn = Utilities::getConnection();
			$objResult = $objConn->query($query);

		}
	}

	/**
	 * Ändert den Eintrag mit der Id aus der Tabelle der Datebank.
	 * @param Variable $table Tabelle auf die zugegriffen werden soll.
	 * @param Variable $id ID des Eintrags der geändert werden soll.
	 * @param Variable $array Daten die eingetragen werden sollen.
	 */
	public static function update($table, $id, $array)
	{
		if (isset($array))
		{
			$keys = "";
			foreach ($array as $key => $value)
			{
				if($keys != "")
				{
					$keys .= ", ";
				}

				$keys .= $key . "=" . ((is_numeric($value))? $value : "'" . $value . "'");
			}
				
			$query = "UPDATE " . $table . " SET " . $keys . " WHERE id=" . $id;
			$objConn = Utilities::getConnection();
			$objResult = $objConn->query($query);
		}
	}

	/**
	 * Löscht den Eintrag mit der Id aus der Tabelle der Datebank.
	 * @param Variable $table Tabelle auf die zugegriffen werden soll.
	 * @param Variable $id ID des Eintrags der gelöscht werden soll.
	 */
	public static function delete($table, $id)
	{
		$query = "DELETE FROM " . $table . " WHERE id=" . $id;
		$objConn = Utilities::getConnection();
		$objResult = $objConn->query($query);
	}
}
?>