<?php
session_start();
?>
<html>
    <head>
        <link rel="stylesheet" href="https://netdna.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <script src="https://netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    </head>
    <body>
    <div class="container">
        <div class="row text-center" style="margin-top:200px">
            <div class="col-sm-6 col-sm-offset-3">
            <br><br> <h2 style="color:#FF5733">Disconnect failed</h2>
            <p style="font-size:20px;color:#5C5C5C;">
            <?php 
            if(isset($_SESSION['error'])){
            echo $_SESSION['error'];
            }
            else{
               echo 'Please try again later';
            }
            ?></p>
            <a href="connect" class="btn btn-success"> Back to connecting page </a>
        <br><br>
            </div>
            
        </div>
    </div>
    </body>
</html>

