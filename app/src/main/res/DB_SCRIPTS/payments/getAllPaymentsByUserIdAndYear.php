<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');
 
include_once '../config/database.php';
include_once '../objects/payment.php';

$database = new Database();
$db = $database->getConnection();
$payment = new Payment($db);
$userId = isset($_GET['userId']) ? $_GET['userId'] : die();
$year = isset($_GET['year']) ? $_GET['year'] : die();
$stmt = $payment->getAllPaymentsByUserIdAndYear($userId, $year);
$payments_arr = array();
while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    array_push($payments_arr, (int)$row['FOR_MONTH']);
}
echo json_encode($payments_arr);
?>
