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

            $mysqli = new mysqli("mysql", "user", "password", "appDB");

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

    <div class="form">
        <form method="POST" style="display: flex">
            <label for="id">ID:</label>
            <input type="number" id="id" name="id" required><br>
            <input type="submit" value="Удалить">
        </form>
    </div>

    <h2>
        <a href="index.html">Главная</a>
        <a href="contacts.html">Контакты</a>
    </h2>
</body>

</html>