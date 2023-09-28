<?php
header("Content-Type: application/json");

require_once 'employee.php';
require_once 'task.php';

$employee = new Employee();
$task = new Task();

$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$uri = explode('/', $uri);

$requestMethod = $_SERVER['REQUEST_METHOD'];

if ($uri[2] === 'task') {
    if ($requestMethod === 'GET') {
        if (isset($uri[3])) {
            $id = $uri[3];
            $data = $task->getTask($id);
        } else {
            $data = $task->getAllTasks();
        }
    } elseif ($requestMethod === 'POST') {
        $postData = json_decode(file_get_contents("php://input"), true);
        $data = $task->addTask($postData);
    } elseif ($requestMethod === 'PATCH') {
        $id = $uri[3];
        $postData = json_decode(file_get_contents("php://input"), true);
        $data = $task->updateTask($id, $postData);
    } elseif ($requestMethod === 'DELETE') {
        $id = $uri[3];
        $data = $task->deleteTask($id);
    }
} elseif ($uri[2] === 'employee') {
    if ($requestMethod === 'GET') {
        if (isset($uri[3])) {
            $id = $uri[3];
            $data = $employee->getEmployee($id);
        } else {
            $data = $employee->getAllEmployees();
        }
    } elseif ($requestMethod === 'POST') {
        $postData = json_decode(file_get_contents("php://input"), true);
        $data = $employee->addEmployee($postData);
    } elseif ($requestMethod === 'PATCH') {
        $id = $uri[3];
        $postData = json_decode(file_get_contents("php://input"), true);
        $data = $employee->updateEmployee($id, $postData);
    } elseif ($requestMethod === 'DELETE') {
        $id = $uri[3];
        $data = $employee->deleteEmployee($id);
    }
}

if ($data) {
    echo json_encode($data);
} else {
    http_response_code(404);
    echo json_encode(array("error" => "Entity not found"));
}
?>