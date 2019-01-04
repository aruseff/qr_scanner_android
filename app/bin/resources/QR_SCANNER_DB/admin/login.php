<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
 
include_once '../config/database.php';
include_once '../objects/admin.php';

$database = new Database();
$db = $database->getConnection();
$admin = new Admin($db);
$username = isset($_GET['username']) ? $_GET['username'] : die();
$password = isset($_GET['password']) ? $_GET['password'] : die();
$success = $admin->login($username, $password);
echo json_encode($success);
?>
