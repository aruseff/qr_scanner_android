<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once '../config/database.php';
include_once '../objects/user.php';

$database = new Database();
$db       = $database->getConnection();
$user  = new User($db);
$stmt     = $user->getAllUsers();

$user_arr            = array();

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    extract($row);
    $user = array(
        "INTERNAL_ID" => $INTERNAL_ID,
        "NAME" => $NAME,
        "EXTERNAL_ID" => $EXTERNAL_ID,
        "CREATION_DATE" => $CREATION_DATE
    );
    array_push($user_arr, $user);
}

echo json_encode($user_arr);
?>
