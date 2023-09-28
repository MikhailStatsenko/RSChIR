<html lang="ru">

<head>
    <meta charset="UTF-8">
    <title>Read</title>
    <link type="text/css" rel="stylesheet" href="style.css" />
</head>

<body>
    <h1>
        <a href="create.php">C</a>
        <a href="index.php">R</a>
        <a href="update.php">U</a>
        <a href="delete.php">D</a>
    </h1>
    <div>
        <table>
            <tr>
                <th>Id</th>
                <th>Название</th>
                <th>Цена</th>
            </tr>
            <?php
                $mysqli = new mysqli("db", "user", "password", "appDB");

                $res = $mysqli->query("SELECT * FROM products");
                foreach ($res as $row){
                    echo "<tr><td>{$row['id']}</td><td>{$row['name']}</td><td>{$row['price']}</td></tr>";
                }

                $mysqli->close();
            ?>
        </table>
    </div>
</body>

</html>

