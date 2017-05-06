<?php

	header("Content-Type: application/json");
	header('Access-Control-Allow-Origin: *');  
	header('Access-Control-Allow-Methods: GET, PUT, POST, DELETE, OPTIONS');
	header('Access-Control-Allow-Headers: Content-Type, Content-Range, Content-Disposition, Content-Description');

	$fb_access_token = "access_token=EAAC7itmOQVwBAFuZCLZBJBvZBTZCtEUTTHjvMOxHRC109HglOwTlPyDkujMWOOkKcxMn2CHGWfRk9LY76jejptskCUYAHq97nlv03iJAZBL6Fx0IBQGvflOVZCxZAruqEkyT9T8zVKRYHrTHxs3fguHYlhsriafytUZD";
	$fb_search_url = "https://graph.facebook.com/v2.8/";
	$prelim_field = "fields=id,name,picture.width(700).height(700)";
	$album_posts_field = "fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name,picture}},posts.limit(5){message,story,created_time}";
	$id_field = "fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name,picture}},posts.limit(5)";
	$center_field = "center=";
 
	if(isset($_GET["query"]) && !empty($_GET["query"])){
		//Fetch JSON
		if(!isset($_GET["latitude"]) || empty($_GET["latitude"]) || !isset($_GET["longitude"]) || empty($_GET["longitude"]))
			$url = $fb_search_url."search?q=".$_GET["query"]."&type=".$_GET["type"]."&".$prelim_field."&".$fb_access_token;
		else
			$url = $fb_search_url."search?q=".$_GET["query"]."&type=".$_GET["type"]."&".$prelim_field."&".$center_field.$_GET["latitude"].",".$_GET["longitude"]."&".$fb_access_token;
		$json_response = file_get_contents($url);
		$response = array();

		if($json_response){
			$response = array(
				'status' => true,
				'data' => $json_response
			);
		}
		else{
			$response = array(
				'status' => false
			);
		}

		echo json_encode($response);
	}
	else if(isset($_GET["id"]) && !empty($_GET["id"])){
		$url = $fb_search_url.$_GET["id"]."?".$album_posts_field."&".$fb_access_token;
		$json_response = file_get_contents($url);
		$interim_json = json_decode($json_response, true);

		for($i = 0; $i<sizeof($interim_json["albums"]["data"]); $i+=1){
			for($j = 0; $j<sizeof($interim_json["albums"]["data"][$i]["photos"]["data"]); $j+=1){
				$url = $fb_search_url.$interim_json["albums"]["data"][$i]["photos"]["data"][$j]["id"]."?fields=images"."&".$fb_access_token;
				$temp = json_decode(file_get_contents($url), true);
				$interim_json["albums"]["data"][$i]["photos"]["data"][$j]["picture"] = $temp["images"][0]["source"];
			}
		}

		$response = array();

		if($json_response){
			$response = array(
				'status' => true,
				'data' => json_encode($interim_json)
			);
		}
		else{
			$response = array(
				'status' => false
			);
		}

		echo json_encode($response);	
	}

?>