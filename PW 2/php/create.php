<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Create</title>
    <link type="text/css" rel="stylesheet" href="style.css" />
</head>

<body>
    <?php
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = $_POST['name'];
            $price = $_POST['price'];

            $mysqli = new mysqli("db", "user", "password", "appDB");

            $sql = "INSERT INTO products (name, price) VALUES ('$name', $price)";

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
        <form method="POST">
            <label for="name">Название товара:</label>
            <input type="text" id="name" name="name" required><br>
            <label for="price">Цена товара:</label>
            <input type="number" id="price" name="price" required><br>
            <input type="submit" value="Создать">
        </form>
    </div>
</body>

</html>


