
<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>Test</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">

</head>

<body>

   <h2>Test</h2>
   <form action="google-authenticate.php" method="POST">
   <div class="row form-group" style="margin-left: 30px">
       <input type="email" class="form-control" id="email" name="email" />    
	    <input id="connect" name="action" type="submit" value="Connect"/>
		<input type="submit" name="action" value="Disconnect" id="Disconnect"/>

   </div>
   </form>
    

</body>


</html>