<?php
require_once __DIR__.'/vendor/autoload.php';
session_start();

	if(isset($_GET['email'])){
	  
    	$url = 'https://rkzu4v25bc.execute-api.us-east-1.amazonaws.com/prod/connect';
    	$practitioner_email = $_GET['email'];
    	pushDataToLambda($url,$practitioner_email);
    }
	else 
	{
	    echo $_GET['email'];
    //	header('Location: '.$callback_url.'disconnect-failure.php');
	}
	
	function pushDataToLambda($url,$practitioner_email)
	{
						$http_request =  new Guzzle\Http\Client();
						$http_request->setDefaultOption('exceptions', false);
						$request = $http_request->post($url,array(
									'content-type' => 'application/json'
							),array());
						$data = ['params' =>['email'=> $practitioner_email],
						'action'=>'disconnect'
						];
						$request->setBody( json_encode($data));
						
						$response = $request->send();
						$status_code = var_export($response->getStatusCode(), true);
						$body = $response->getBody();
					    $json = json_decode($body, true);
						$callback_url='https://' . $_SERVER['HTTP_HOST'] .'/practitioner-register/';
						if($status_code==200){
							header('Location: '.$callback_url.'disconnect-success.php');	
						}
						else
						{
						   $_SESSION['error'] =   $json['error'];
							header('Location: '.$callback_url.'disconnect-failure.php');
							
						}
  }
?>