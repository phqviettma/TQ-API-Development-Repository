<?php
require_once __DIR__.'/vendor/autoload.php';
session_start();
try{	    
	$url ="https://ej21ky4mk4.execute-api.us-east-1.amazonaws.com/prod/api";
	$headers_array = getallheaders();
	if(isset($headers_array['X-Goog-Channel-Id'])&&isset($headers_array['X-Goog-Resource-State']))
	{
	    
	        // Buffer all upcoming output...
            ob_start();
            // Get the size of the output.
            $size = ob_get_length();
            // Disable compression (in case content length is compressed).
            header('HTTP/1.1 200 OK', true, 200);
            header("Content-Encoding: none");
            // Set the content length of the response.
            header("Content-Length: {$size}");
            // Close the connection.
            header("Connection: close");
            // Flush all output.
            ob_end_flush();
            ob_flush();
            flush();
            
	    	$http_request =  new Guzzle\Http\Client();
			$request = $http_request->post($url,array(
									'content-type' => 'application/json'
						),array());
			$data = ['googleChannelId' => $headers_array['X-Goog-Channel-Id'],
			'googleChannelToken'=>$headers_array['X-Goog-Channel-Token'],
			
			'googleChannelExpiration'=>$headers_array['X-Goog-Channel-Expiration'],
			
			'googleResourceId' =>$headers_array['X-Goog-Resource-Id'],
			
			'googleResourceState'=>$headers_array['X-Goog-Resource-State'],
			
			'googleResourceUri'=>$headers_array['X-Goog-Resource-Uri'],
			
			'googleMessageNumber'=>$headers_array['X-Goog-Message-Number']
			];
			$request->setBody( json_encode($data));
			$response = $request->send();
			$status_code = var_export($response->getStatusCode(), true);
			if($status_code=200){
			    
			    $arr = array('succeeded' => 'true');
			    header('HTTP/1.1 200 OK', true, 200);
			    echo json_encode($arr);
			}
			else{
			    
			    $arr = array('succeeded' => 'false', 'error'=> $response);
	        	header('HTTP/1.1 400 Bad request', true, $status_code);
			    echo json_encode($arr);
			}
			
	}
	else{
	      $arr = array('succeeded' => 'false', 'error'=> 'Wrong request');
	      header("Status: 404 Not Found");
	      echo json_encode($arr);
	}
}
catch(Google_Service_Exception $e){
	error_log("Have error". $e->getMessage());
}
?>