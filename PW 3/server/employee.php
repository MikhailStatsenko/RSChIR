<?php
$conn = new mysqli("db", "user", "password", "appDB");

class Employee {
    private $conn;

    public function __construct() {
        global $conn;
        $this->conn = $conn;
    }

    public function getEmployee($id) {
        $sql = "SELECT * FROM employee WHERE id = $id";
        $result = $this->conn->query($sql);

        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            return $row;
        } else {
            return null;
        }
    }

    public function getAllEmployees() {
        $sql = "SELECT * FROM employee";
        $result = $this->conn->query($sql);

        $employees = array();
        while ($row = $result->fetch_assoc()) {
            $employees[] = $row;
        }

        return $employees;
    }

    public function addEmployee($data) {
        $name = $data['name'];
        $phone_number = $data['phone_number'];
    
        $sql = "INSERT INTO employee (name, phone_number) VALUES ('$name', '$phone_number')";
        if ($this->conn->query($sql) === TRUE) {
            $newEmployeeId = $this->conn->insert_id;
            return $this->getEmployee($newEmployeeId);
        } else {
            return null;
        }
    }

    public function updateEmployee($id, $data) {
        $name = $data['name'];
        $phone_number = $data['phone_number'];
    
        $sql = "UPDATE employee SET name = '$name', phone_number = '$phone_number' WHERE id = $id";
    
        if ($this->conn->query($sql) === TRUE) {
            return array("message" => "Employee updated successfully");
        } else {
            return array("error" => "Error updating employee: " . $this->conn->error);
        }
    }

    public function deleteEmployee($id) {
        $sql = "DELETE FROM employee WHERE id = $id";
        if ($this->conn->query($sql) === TRUE) {
            return array("message" => "Employee deleted successfully");
        } else {
            return array("error" => "Error deleting employee: " . $this->conn->error);
        }
    }
}
?>