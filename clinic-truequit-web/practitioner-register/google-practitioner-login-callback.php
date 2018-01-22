<?php
require_once __DIR__.'/vendor/autoload.php';
session_start();

$client = new Google_Client();
$client->setAuthConfig('client_secrets.json');
$client->setScopes(array(
    "https://www.googleapis.com/auth/calendar",
    "https://www.googleapis.com/auth/userinfo.profile",
	"https://www.googleapis.com/auth/userinfo.email"
));
$plus = new Google_Service_Oauth2($client);
$service = new Google_Service_Calendar($client);



  try
  {	
		$credential = $client->authenticate($_GET['code']);
		$access_token = $credential['access_token'];
		$refresh_token = $client->getRefreshToken();
		$user = $plus->userinfo->get();
		$email= filter_var($user['email'], FILTER_SANITIZE_EMAIL);
		$name = filter_var($user['name'], FILTER_SANITIZE_SPECIAL_CHARS);
		$last_name = filter_var($user['givenName'], FILTER_SANITIZE_SPECIAL_CHARS);
	  	$first_name = filter_var($user['familyName'], FILTER_SANITIZE_SPECIAL_CHARS);
		$url = 'https://9a87rzr9jd.execute-api.us-east-1.amazonaws.com/prod/connect';
		if(isset($_SESSION['email'])){
			$user_email = $_SESSION['email']; 
			pushDataToLambda($url,$user_email,$email,$last_name,$first_name,$name,$access_token,$refresh_token);
			
		}
		else{
			pushDataToLambda($url,$email,$email,$last_name,$first_name,$name,$access_token,$refresh_token);
			
		}
		
  }
  catch(Exception  $e) {
       $arr = array('succeeded' => 'false', 'error'=> 'Wrong request');
	  header('HTTP/1.1 400 Bad request', true, 400);
	   echo json_encode($arr);
	 error_log("Have error ". $e->getMessage());
 
  }
  catch(Google_Service_Exception $gse){
      $arr = array('succeeded' => 'false', 'error'=> 'Wrong request');
	  header('HTTP/1.1 500 Internal Server', true, 500);
	   echo json_encode($arr);
      error_log("Have error ". $gse->getMessage());
  }
  function pushDataToLambda($url,$practitioner_email,$google_email,$practitioner_lastName, $practitioner_firstName,$practitioner_name,$access_token, $refresh_token)
	{
						$http_request =  new Guzzle\Http\Client();
						$http_request->setDefaultOption('exceptions', false);
						$request = $http_request->post($url,array(
									'content-type' => 'application/json'
							),array());
						$data = ['email' => $practitioner_email,
						'lastName'  => $practitioner_lastName,
						'firstName' => $practitioner_firstName,
						'name' => $practitioner_name,
						'accessToken'=>$access_token,
						'refreshToken'=>$refresh_token,
						'googleEmail'=>$google_email
						];
						$request->setBody( json_encode($data));
						
						$response = $request->send();
						$status_code = var_export($response->getStatusCode(), true);
						$body = $response->getBody();
					    $json = json_decode($body, true);
					
					 
						$callback_url='https://' . $_SERVER['HTTP_HOST'] .'/practitioner-register/';
						if($status_code==200){
							header('Location: '.$callback_url.'success.php');	
						}
						else
						{
						   $_SESSION['error'] =   $json['error'];
							header('Location: '.$callback_url.'failure.php');
							
						}
  }
  
?>