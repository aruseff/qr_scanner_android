<?php

class Payment

{
    private $conn;
    public $ID;
    public $CREATION_DATE;
    public $FOR_MONTH;
    public $FOR_YEAR;
    public $FK_USER_ID;

    public function __construct($db) {
        $this->conn = $db;
    }

    function addPayment($userId, $forMonth, $forYear) {
        $query = "INSERT INTO PAYMENT SET FK_USER_ID=?, FOR_MONTH=?, FOR_YEAR=?";
        $stmt = $this->conn->prepare($query);
        $userId = htmlspecialchars(strip_tags($userId));
        $forMonth = htmlspecialchars(strip_tags($forMonth));
        $forYear = htmlspecialchars(strip_tags($forYear));
        $stmt->bindParam(1, $userId);
        $stmt->bindParam(2, $forMonth);
        $stmt->bindParam(3, $forYear);
        return $stmt->execute();
    }

    function getAllPaymentsByUserIdAndYear($userId, $forYear) {
        $query = "SELECT ID, CREATION_DATE, FOR_MONTH, FOR_YEAR, FK_USER_ID FROM PAYMENT WHERE FK_USER_ID=? AND FOR_YEAR=? ORDER BY FOR_YEAR ASC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $userId);
        $stmt->bindParam(2, $forYear);
        $stmt->execute();
        return $stmt;
    }
}
