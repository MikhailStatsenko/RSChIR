<?php
$conn = new mysqli("db", "user", "password", "appDB");

class Task {
    private $conn;

    public function __construct() {
        global $conn;
        $this->conn = $conn;
    }

    public function getTask($id) {
        $sql = "SELECT * FROM task WHERE id = $id";
        $result = $this->conn->query($sql);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            return $row;
        } else {
            return null;
        }
    }

    public function getAllTasks() {
        $sql = "SELECT * FROM task";
        $result = $this->conn->query($sql);

        $tasks = array();
        while ($row = $result->fetch_assoc()) {
            $tasks[] = $row;
        }

        return $tasks;
    }

    public function addTask($data) {
        $title = $data['title'];
        $description = $data['description'];
        $employee_id = $data['employee_id'];
    
        $sql = "INSERT INTO task (title, description, employee_id) VALUES ('$title', '$description', $employee_id)";
        if ($this->conn->query($sql) === TRUE) {
            $newTaskId = $this->conn->insert_id;
            return $this->getTask($newTaskId);
        } else {
            return array("error" => "Error creating task: " . $this->conn->error);
        }
    }
    
    public function updateTask($id, $data) {
        $title = $data['title'];
        $description = $data['description'];
        $employee_id = $data['employee_id'];
    
        $sql = "UPDATE task SET title = '$title', description = '$description', employee_id = $employee_id WHERE id = $id";
        if ($this->conn->query($sql) === TRUE) {
            return array("message" => "Task updated successfully");
        } else {
            return array("error" => "Error updating task: " . $this->conn->error);
        }
    }

    public function deleteTask($id) {
        $sql = "DELETE FROM task WHERE id = $id";
        
        if ($this->conn->query($sql) === TRUE) {
            return array("message" => "Task deleted successfully");
        } else {
            return array("error" => "Error deleting task: " . $this->conn->error);
        }
    }
}
?>