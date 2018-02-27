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

	    if ($_POST['email']==null && isset($_POST['action']) && $_POST['action']=='Connect'){
	        
    		$auth_url = $client->createAuthUrl();
	    	header('Location: ' . filter_var($auth_url, FILTER_SANITIZE_URL));
		}
	    else if(isset($_POST['action']) && $_POST['action']=='Connect'){
    		
    		$_SESSION['email'] =  $_POST['email'];
    		$auth_url = $client->createAuthUrl();
    		header('Location: ' . filter_var($auth_url, FILTER_SANITIZE_URL));
		}
		
		else if($_POST['email']==null && isset($_POST['action']) && $_POST['action']=='Disconnect'){
			
			header('Location: '.$callback_url.'disconnect-failure.php');
		}
    	else if(isset($_POST['action']) && $_POST['action']=='Disconnect'){
    	    $_SESSION['email'] =  $_POST['email'];
		    header('Location: '.$callback_url.'disconnect.php');
		}
	
		else{
		    	header('Location: '.$callback_url.'failure.php');
		}
	
    
	
	
	


?>
