<!DOCTYPE html>
<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Update</title>
    <link type="text/css" rel="stylesheet" href="style.css" />
</head>

<body>
    <?php
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $id = $_POST['id'];
            $name = $_POST['name'];
            $price = $_POST['price'];

            $mysqli = new mysqli("mysql", "user", "password", "appDB");

            $sql = "UPDATE products SET name='$name', price=$price WHERE id=$id";

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
        <form method="POST">
            <label for="id">ID:</label>
            <input type="number" id="id" name="id" required><br>
            <label for="name">Новое название:</label>
            <input type="text" id="name" name="name" required><br>
            <label for="price">Новая цена:</label>
            <input type="number" id="price" name="price" required><br>
            <input type="submit" value="Обновить">
        </form>
    </div>

    <h2>
        <a href="index.html">Главная</a>
        <a href="contacts.html">Контакты</a>
    </h2>
</body>



</html>