<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>Test</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.5/angular.min.js"></script>
 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css">
 <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert-dev.js"></script>
</head>

<body>

   <h2>Test</h2>
   <form action="" method="POST">
   <div class="row form-group" style="margin-left: 30px">
       <input type="email" class="form-control" id="email" name="email" />    
	    <input id="connect" name="action" type="submit" value="Connect"/>
		<button type="button" class="btn btn-success " onclick="btnDisconnect()">Test</button>
   </form>
    <script>
    function btnDisconnect(){
   
     swal({
        title: "Are you sure you want to disconnect?",
        text: "",
        type: "warning",
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes",
        showCancelButton: true,
        closeOnConfirm: true
        }, function() {
        confirmAlert();
        });
    
    }
     function confirmAlert() {
         var a = document.getElementById("email").value;
       location.href="google-authenticate.php?email="+a+"&status=disconnect";
    }
    </script>

</body>


</html>