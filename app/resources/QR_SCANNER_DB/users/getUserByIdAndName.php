<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
 
include_once '../config/database.php';
include_once '../objects/user.php';

$database = new Database();
$db = $database->getConnection();
$user = new User($db);
$id = isset($_GET['id']) ? $_GET['id'] : die();
$name = isset($_GET['name']) ? $_GET['name'] : die();
$stmt = $user->getUserByIdAndName($id, $name);

if($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $user->INTERNAL_ID = $row['INTERNAL_ID'];
    $user->NAME = $row['NAME'];
    $user->EXTERNAL_ID = $row['EXTERNAL_ID'];
    $user->CREATION_DATE = $row['CREATION_DATE'];
    print_r(json_encode($user));
} else {
    echo 'USER_NOT_FOUND';
}
?>
