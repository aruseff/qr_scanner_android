<?php

class User

{
    private $conn;
    public $INTERNAL_ID;
    public $NAME;
    public $EXTERNAL_ID;
    public $CREATION_DATE;

    public function __construct($db) {
        $this->conn = $db;
    }

    function addUser($externalId, $name) {
        $query = "INSERT INTO USER SET EXTERNAL_ID=?, NAME=?";
        $stmt = $this->conn->prepare($query);
        $externalId = htmlspecialchars(strip_tags($externalId));
        $name = htmlspecialchars(strip_tags($name));
        $stmt->bindParam(1, $externalId);
        $stmt->bindParam(2, $name);
        return $stmt->execute();
    }

    function getAllUsers() {
        $query = "SELECT INTERNAL_ID, NAME, EXTERNAL_ID, CREATION_DATE FROM USER ORDER BY NAME ASC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    function getUserById($id) {
        $query = "SELECT INTERNAL_ID, NAME, EXTERNAL_ID, CREATION_DATE FROM USER WHERE EXTERNAL_ID=?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);
        $stmt->execute();
        return $stmt;
    }

    function getUserByIdAndName($id, $name) {
        $query = "SELECT INTERNAL_ID, NAME, EXTERNAL_ID, CREATION_DATE FROM USER WHERE EXTERNAL_ID=? AND NAME=?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $id);
        $stmt->bindParam(2, $name);
        $stmt->execute();
        return $stmt;
    }
    
    function getUsersByName($name) {
        $query = "SELECT INTERNAL_ID, NAME, EXTERNAL_ID, CREATION_DATE FROM USER WHERE NAME LIKE ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindValue(1, "%$name%", PDO::PARAM_STR);
        $stmt->execute();
        return $stmt;
    }
}
