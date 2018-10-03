<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
include_once '../config/database.php';
include_once '../objects/payment.php';
 
$database = new Database();
$db = $database->getConnection();
$payment = new Payment($db);
$data = json_decode(file_get_contents("php://input"));
 
$userId = $data->userId;
$forMonth = $data->forMonth;
$forYear = $data->forYear;
 
if($payment->addPayment($userId, $forMonth, $forYear)){
    echo 'OK';
} else{
    echo 'FAIL';
}
?>
