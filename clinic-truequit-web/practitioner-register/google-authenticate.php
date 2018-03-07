<?php
require_once __DIR__.'/vendor/autoload.php';

session_start();

$client = new Google_Client();
$client->setAuthConfigFile('client_secrets.json');
$client->setRedirectUri('https://' . $_SERVER['HTTP_HOST'] .'/practitioner-register/google-practitioner-login-callback.php');
$client->setAccessType('offline');        // offline access
$client->setApprovalPrompt('force');
$client->setScopes(array(
    "https://www.googleapis.com/auth/calendar",
    "https://www.googleapis.com/auth/userinfo.profile",
	"https://www.googleapis.com/auth/userinfo.email"
));
        if(isset($_GET['email'])){
            
            $_SESSION['email'] =  $_GET['email'];
    		$auth_url = $client->createAuthUrl();
    		header('Location: ' . filter_var($auth_url, FILTER_SANITIZE_URL));
        }
        else{
            
            $auth_url = $client->createAuthUrl();
	    	header('Location: ' . filter_var($auth_url, FILTER_SANITIZE_URL));
        }
	 

    
	
	
	


?>
