<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Delete</title>
    <link type="text/css" rel="stylesheet" href="style.css" />
</head>

<body>
    <?php
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $id = $_POST['id'];

            $mysqli = new mysqli("db", "user", "password", "appDB");

            $sql = "DELETE FROM products WHERE id=$id";
            $mysqli->query($sql);

            $mysqli->close();
        }
    ?>
    <h1>
        <a href="create.php">C</a>
        <a href="index.php">R</a>
        <a href="update.php">U</a>
        <a href="delete.php">D</a>
    </h1>

    <div>
        <form method="POST" style="display: flex">
            <label for="id">ID:</label>
            <input type="number" id="id" name="id" required><br>
            <input type="submit" value="Удалить">
        </form>
    </div>
    
</body>
</html>

