<?php

class Admin

{
    private $conn;
    public $ID;
    public $USERNAME;
    public $PASSWORD;

    public function __construct($db) {
        $this->conn = $db;
    }

    function login($username, $password) {
        $query = "SELECT COUNT(ID) AS login FROM ADMIN WHERE USERNAME=? AND PASSWORD=PASSWORD(?)";
        $stmt = $this->conn->prepare($query);
        $username = htmlspecialchars(strip_tags($username));
        $password = htmlspecialchars(strip_tags($password));
        $stmt->bindParam(1, $username);
        $stmt->bindParam(2, $password);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        return $row['login'] == "1";
    }
}
?>
