var napp = angular.module('hw8', ['ngAnimate']);

//User Table Generator Controller
napp.controller('userTableGenerator', function($scope){
	$scope.entries = null;
	$scope.show = [];
	$scope.show['user'] = false;
	$scope.showPrevButton = false;
	$scope.showNextButton = false;
	$scope.prevLink = "";
	$scope.nextLink = "";
	$scope.toggleArr = [];

	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";
	$scope.init = true;

	$scope.toggleFavButton = function(id){
		if($scope.id!="" || $scope.init==true){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";

				for(var i = 0; i<$scope.entries.data.length; i++){
					if($scope.entries.data[i].id == id){
						value += $scope.entries.data[i].picture.data.url;
						value += " ";
						value += $scope.entries.data[i].name.replace(/ /g, "+");
						value += " users ";
						value += id;
						localStorage.setItem(id, value);
						break;
					}
				}
			}
			else{
				localStorage.removeItem(id);
			}
		}
	};

	$scope.accessAlbumsAndPosts = function(id){
		$scope.init = false;
		$scope.show['user'] = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = null;
		$scope.postName = null;
		$scope.id = "";
		fetchAlbumsAndPostsFromServer(id, 'user', function(albs, posts, name, picture){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.albumEntries = albs;
					$scope.postEntries = posts;
					$scope.postName = name;
					$scope.postPicture = picture;
				});
			}
			else{
				$scope.albumEntries = albs;
				$scope.postEntries = posts;
				$scope.postName = name;
				$scope.postPicture = picture;
			}
		});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.show['user'] = true;
	}

	$scope.gotoNextPage = function(link){
		console.log('Reached ' + link);
		fetchFbDataFromServer('user', link);
	};

	$scope.gotoPrevPage = function(link){
		fetchFbDataFromServer('user', link);
	}
});

//Page Table Generator Controller
napp.controller('pageTableGenerator', function($scope, $timeout){
	$scope.entries = null;
	$scope.show = [];
	$scope.show['page'] = false;
	$scope.showPrevButton = false;
	$scope.showNextButton = false;
	$scope.prevLink = "";
	$scope.nextLink = "";
	$scope.toggleArr = [];

	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";
	$scope.init = true;

	$scope.toggleFavButton = function(id){
		if($scope.id!="" || $scope.init==true){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";

				for(var i = 0; i<$scope.entries.data.length; i++){
					if($scope.entries.data[i].id == id){
						value += $scope.entries.data[i].picture.data.url;
						value += " ";
						value += $scope.entries.data[i].name.replace(/ /g, "+");
						value += " pages ";
						value += id;
						localStorage.setItem(id, value);
						break;
					}
				}
			}
			else{
				localStorage.removeItem(id);
			}
		}
	};

	$scope.accessAlbumsAndPosts = function(id){
		$scope.init = false;
		$scope.show['page'] = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = null;
		$scope.postName = null;
		$scope.id = "";
		fetchAlbumsAndPostsFromServer(id, 'page', function(albs, posts, name, picture){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.albumEntries = albs;
					$scope.postEntries = posts;
					$scope.postName = name;
					$scope.postPicture = picture;
				});
			}
			else{
				$scope.albumEntries = albs;
				$scope.postEntries = posts;
				$scope.postName = name;
				$scope.postPicture = picture;
			}
		});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.show['page'] = true;
	}

	$scope.gotoNextPage = function(link){
		console.log('Reached ' + link);
		fetchFbDataFromServer('page', link);
	};

	$scope.gotoPrevPage = function(link){
		fetchFbDataFromServer('page', link);
	}
});

//Event Table Generator Controller
napp.controller('eventTableGenerator', function($scope){
	$scope.entries = null;
	$scope.show = [];
	$scope.show['event'] = false;
	$scope.showPrevButton = false;
	$scope.showNextButton = false;
	$scope.prevLink = "";
	$scope.nextLink = "";
	$scope.toggleArr = [];

	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";
	$scope.init = true;

	$scope.toggleFavButton = function(id){
		if($scope.id!="" || $scope.init==true){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";

				for(var i = 0; i<$scope.entries.data.length; i++){
					if($scope.entries.data[i].id == id){
						value += $scope.entries.data[i].picture.data.url;
						value += " ";
						value += $scope.entries.data[i].name.replace(/ /g, "+");
						value += " events ";
						value += id;
						localStorage.setItem(id, value);
						break;
					}
				}
			}
			else{
				localStorage.removeItem(id);
			}
		}
	};

	$scope.accessAlbumsAndPosts = function(id, name, picture){
		$scope.init = false;
		$scope.show['event'] = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = picture;
		$scope.postName = name;
		$scope.id = id;
		fetchAlbumsAndPostsFromServer(id, 'event', function(albs, posts, name, picture){});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.show['event'] = true;
	}

	$scope.gotoNextPage = function(link){
		console.log('Reached ' + link);
		fetchFbDataFromServer('event', link);
	};

	$scope.gotoPrevPage = function(link){
		fetchFbDataFromServer('event', link);
	}
});

//Place Table Generator Controller
napp.controller('placeTableGenerator', function($scope){
	$scope.entries = null;
	$scope.show = [];
	$scope.show['place'] = false;
	$scope.showPrevButton = false;
	$scope.showNextButton = false;
	$scope.prevLink = "";
	$scope.nextLink = "";
	$scope.toggleArr = [];

	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";
	$scope.init = true;

	$scope.toggleFavButton = function(id){
		if($scope.id!="" || $scope.init==true){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";

				for(var i = 0; i<$scope.entries.data.length; i++){
					if($scope.entries.data[i].id == id){
						value += $scope.entries.data[i].picture.data.url;
						value += " ";
						value += $scope.entries.data[i].name.replace(/ /g, "+");
						value += " places ";
						value += id;
						localStorage.setItem(id, value);
						break;
					}
				}
			}
			else{
				localStorage.removeItem(id);
			}
		}
	};

	$scope.accessAlbumsAndPosts = function(id){
		$scope.init = false;
		$scope.show['place'] = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = null;
		$scope.postName = null;
		$scope.id = "";
		fetchAlbumsAndPostsFromServer(id, 'place', function(albs, posts, name, picture){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.albumEntries = albs;
					$scope.postEntries = posts;
					$scope.postName = name;
				    $scope.postPicture = picture;
				});
			}
			else{
				$scope.albumEntries = albs;
				$scope.postEntries = posts;
				$scope.postName = name;
				$scope.postPicture = picture;
			}
		});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.show['place'] = true;
	}

	$scope.gotoNextPage = function(link){
		console.log('Reached ' + link);
		fetchFbDataFromServer('place', link);
	};

	$scope.gotoPrevPage = function(link){
		fetchFbDataFromServer('place', link);
	}
});

//Group Table Generator Controller
napp.controller('groupTableGenerator', function($scope){
	$scope.entries = null;
	$scope.show = [];
	$scope.show['group'] = false;
	$scope.showPrevButton = false;
	$scope.showNextButton = false;
	$scope.prevLink = "";
	$scope.nextLink = "";
	$scope.toggleArr = [];

	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";
	$scope.init = true;

	$scope.toggleFavButton = function(id){
		if($scope.id!="" || $scope.init==true){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";

				for(var i = 0; i<$scope.entries.data.length; i++){
					if($scope.entries.data[i].id == id){
						value += $scope.entries.data[i].picture.data.url;
						value += " ";
						value += $scope.entries.data[i].name.replace(/ /g, "+");
						value += " groups ";
						value += id;
						localStorage.setItem(id, value);
						break;
					}
				}
			}
			else{
				localStorage.removeItem(id);
			}
		}
	};

	$scope.returnExist = function(entry){
		alert(entry.name + " " + entry.id);
		return true;
	}

	$scope.accessAlbumsAndPosts = function(id){
		$scope.init = false;
		$scope.show['group'] = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = null;
		$scope.postName = null;
		$scope.id = "";
		
		fetchAlbumsAndPostsFromServer(id, 'group', function(albs, posts, name, picture){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.albumEntries = albs;
					$scope.postEntries = posts;
					$scope.postName = name;
					$scope.postPicture = picture;
				});
			}
			else{
				$scope.albumEntries = albs;
				$scope.postEntries = posts;
				$scope.postName = name;
				$scope.postPicture = picture;
			}
		});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.show['group'] = true;
	}

	$scope.gotoNextPage = function(link){
		console.log('Reached ' + link);
		fetchFbDataFromServer('group', link);
	};

	$scope.gotoPrevPage = function(link){
		fetchFbDataFromServer('group', link);
	}
});

//Favorite Table Generator Controller
napp.controller('favoriteTableGenerator', function($scope){
	$scope.entries = {};
	$scope.entries.data = [];
	$scope.toggleArr = [];
	$scope.showFav = false;

	$scope.type = "";
	$scope.picture = null;
	$scope.name = null;
	$scope.showAlbsPosts = false;
	$scope.slideFromRight = false;
	$scope.albumEntries = null;
	$scope.postEntries = null;
	$scope.no_data_album = false;
	$scope.accordion = false;
	$scope.no_data_post = false;
	$scope.post_data = false;
	$scope.postPicture = null;
	$scope.postName = null;
	$scope.id = "";

	$scope.accessAlbumsAndPosts = function(id, picture, name, entryType){
		$scope.toggleArr[id] = true;
		$scope.type = entryType;
		$scope.picture = picture;
		$scope.name = name;
		$scope.showFav = false;
		$scope.showAlbsPosts = true;
		$scope.slideFromRight = true;
		$scope.albumEntries = null;
		$scope.postEntries = null;
		$scope.no_data_album = false;
		$scope.accordion = false;
		$scope.no_data_post = false;
		$scope.post_data = false;
		$scope.postPicture = null;
		$scope.postName = null;
		$scope.id = "";
		fetchAlbumsAndPostsFromServer(id, 'favorite', function(albs, posts, name, picture){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					$scope.albumEntries = albs;
					$scope.postEntries = posts;
					$scope.postName = name;
					$scope.postPicture = picture;
				});
			}
			else{
				$scope.albumEntries = albs;
				$scope.postEntries = posts;
				$scope.postName = name;
				$scope.postPicture = picture;
			}
		});
	}

	$scope.returnMessage = function(entry){
		if(entry.message)
			return entry.message;
		else if(entry.story)
			return entry.story;
		return "";
	}

	$scope.returnCurrentState = function(entry){
		if(entry==null || entry===undefined){
			return false;
		}
		else{
			return true;
		}
	}

	$scope.shareOnFb = function(){

		if($scope.postName && $scope.postPicture){
			FB.ui({
				app_id: '1295911343819593',
				method: 'feed',
				link: window.location.href,
				href: 'https://developers.facebook.com/docs/',
				picture: $scope.postPicture,
				name: $scope.postName,
				caption: 'FB SEARCH FROM USC CSCI571'
			}, function(response){
				if(response && !response.error_message){
					alert("Posted");
				}
				else{
					alert("Not posted");
				}
			});
		}
		else{
			alert("Error - Not able to post! Server returned incomplete JSON. Incomplete information.");
		}
	}

	$scope.splitTime = function(timer){
		return (timer.split('T')[0] + ' ' + timer.split('T')[1].split('+')[0]);
	}

	$scope.goBackToTable = function(){
		$scope.showAlbsPosts = false;
		$scope.showFav = true;
	}

	$scope.updateFavorites = function(){
		var value, obj = {};
		$scope.entries.data = [];
		for(var i = 0; i<localStorage.length; i++){
			obj = {};
			value = localStorage.getItem(localStorage.key(i)).split(" ");
			obj.id = value[3];
			obj.picture = value[0];
			obj.name = value[1].split('+').join(' ');
			obj.type = value[2];
			obj.details = value[3];
			$scope.entries.data.push(obj);
		}
	}

	$scope.toggleDelButton = function(id){
		localStorage.removeItem(id);

		for(var i = 0; i<$scope.entries.data.length; i++){
			if($scope.entries.data[i].id == id){
				$scope.entries.data.splice(i, 1);
				break;
			}
		}

		if($scope.entries.data.length==0){
			$scope.showFav = false;
			alert("You have no favorites!");
		}
	}

	$scope.toggleFavButton = function(id){
		if($scope.id!=""){
			$scope.toggleArr[id] = !$scope.toggleArr[id];
			if($scope.toggleArr[id]==true){
				var value = "";
				value += $scope.picture;
				value += " ";
				value += $scope.name.replace(/ /g, "+");
				value += " " + $scope.type + " ";
				value += id;
				localStorage.setItem(id, value);
			}
			else{
				localStorage.removeItem(id);
			}
			$scope.updateFavorites();
		}
	};
});

function fetchFbDataFromServer(type, link){
	invalidateRightSlides();

 	var appElement = document.querySelector('[ng-controller=' + type + 'TableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.show[type] = false;
		});
	}
	else{
		$scope.show[type] = false;
	}

	console.log("OBJ - " + $scope.entries);

	document.getElementById('progressBar').style.display = "block";

	var query = document.getElementById('textFieldTopBar').value.replace(/ /g, "+");
	
	if(query=="" || query==null){
		document.getElementById('progressBar').style.display = "none";
	}
	else{
		//Make AJAX call
		if(link==null){
			if(!$scope.$$phase){
				$scope.$apply(function(){
					if($scope.entries){
						for(var i = 0; i<$scope.entries.data.length; i++){
							$scope.toggleArr[$scope.entries.data[i].id] = false;
							if(localStorage.getItem($scope.entries.data[i].id)){
								$scope.toggleArr[$scope.entries.data[i].id] = true;
							}
						}
						$scope.show[type] = true;
					}
				});
			}
			else{
				if($scope.entries){
					for(var i = 0; i<$scope.entries.data.length; i++){
						$scope.toggleArr[$scope.entries.data[i].id] = false;
						if(localStorage.getItem($scope.entries.data[i].id)){
							$scope.toggleArr[$scope.entries.data[i].id] = true;
						}
					}
					$scope.show[type] = true;
				}
			}
			document.getElementById('progressBar').style.display = "none";		
		}
		else{
			console.log("Here3");
			$.ajax({
				url: link,
				type: 'GET',
				success: function(response){
					if(response){
						var obj = response;
				
						if(!$scope.$$phase){
							$scope.$apply(function(){
								$scope.show[type] = true;
								$scope.entries = obj;
								$scope.toggleArr = [];

								if(obj.paging.previous){
									$scope.prevLink = obj.paging.previous;
									$scope.showPrevButton = true;
								}

								if(obj.paging.next){
									$scope.nextLink = obj.paging.next;
									$scope.showNextButton = true;
								}

								for(var i = 0; i<obj.data.length; i++){
									$scope.toggleArr[obj.data[i].id] = false;
									if(localStorage.getItem(obj.data[i].id)){
										$scope.toggleArr[obj.data[i].id] = true;
									}
								}
							});
						}
						else{
							$scope.show[type] = true;
							$scope.entries = obj;
							$scope.toggleArr = [];

							if(obj.paging.previous){
								$scope.prevLink = obj.paging.previous;
								$scope.showPrevButton = true;
							}

							if(obj.paging.next){
								$scope.nextLink = obj.paging.next;
								$scope.showNextButton = true;
							}

							for(var i = 0; i<obj.data.length; i++){
								$scope.toggleArr[obj.data[i].id] = false;
								if(localStorage.getItem(obj.data[i].id)){
									$scope.toggleArr[obj.data[i].id] = true;
								}
							}
						}
						console.log("Over");
						document.getElementById('progressBar').style.display = "none";
					}
					else{
						//Give error message
						alert('Error: Query could not be processed successfully!');
						document.getElementById('progressBar').style.display = "none";
					}
				},
				error: function(response){
					//Parse error
					document.getElementById('progressBar').style.display = "none";
				}
			});
		}
	}
}

//This helper will be called for globally retrieving data
function helperToFetchFreshData(query, type){
	var appElement = document.querySelector('[ng-controller=' + type + 'TableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.show[type] = false;
		});
	}
	else{
		$scope.show[type] = false;
	}

	document.getElementById('progressBar').style.display = "block";
	console.log("QUERY IS " + query);
	console.log("TYPE IS " + type);
	
	//Make AJAX call
	console.log("AJAX");
	$.ajax({
		url: 'http://samp-app.us-west-2.elasticbeanstalk.com',
		data: {
			'query': query,
			'type': type
		},
		type: 'GET',
		success: function(response){
			if(response.status){
				console.log("The Query Is " + response.data);
				var obj = JSON.parse(response.data);

				if(!$scope.$$phase){
					$scope.$apply(function(){
						$scope.show[type] = true;
						$scope.entries = obj;
						$scope.toggleArr = [];

						if(obj.paging.previous){
							$scope.prevLink = obj.paging.previous;
							$scope.showPrevButton = true;
						}

						if(obj.paging.next){
							$scope.nextLink = obj.paging.next;
							$scope.showNextButton = true;
						}

						for(var i = 0; i<obj.data.length; i++){
							$scope.toggleArr[obj.data[i].id] = false;
							if(localStorage.getItem(obj.data[i].id)){
								$scope.toggleArr[obj.data[i].id] = true;
							}
						}
					});
				}
				else{
					$scope.show[type] = true;
					$scope.entries = obj;
					$scope.toggleArr = [];

					if(obj.paging.previous){
						$scope.prevLink = obj.paging.previous;
						$scope.showPrevButton = true;
					}

					if(obj.paging.next){
						$scope.nextLink = obj.paging.next;
						$scope.showNextButton = true;
					}

					for(var i = 0; i<obj.data.length; i++){
						$scope.toggleArr[obj.data[i].id] = false;
						if(localStorage.getItem(obj.data[i].id)){
							$scope.toggleArr[obj.data[i].id] = true;
						}
					}
				}
				console.log("Over");
				document.getElementById('progressBar').style.display = "none";
			}
			else{
				//Give error message
				alert('Error: Query could not be processed successfully!');
				document.getElementById('progressBar').style.display = "none";
			}
		},
		error: function(response){
			//Parse error
			alert('Error: AJAX error!');
			document.getElementById('progressBar').style.display = "none";
		}
	});
}

function fetchGlobalFbDataFromServer(){
	invalidateRightSlides();
	var query = document.getElementById('textFieldTopBar').value.replace(/ /g, "+");
	if(query=="" || query==null){
		$("#textFieldTopBar").tooltip("show");
		setTimeout(function(){$("#textFieldTopBar").tooltip("hide");}, 2000);
	}
	else{
		helperToFetchFreshData(query, 'user');
		helperToFetchFreshData(query, 'page');
		helperToFetchFreshData(query, 'event');
		helperToFetchFreshData(query, 'group');
		fetchFbPlacesDataFromServer();
	} 
}

function fetchFbPlacesDataFromServer(){
	if(navigator.geolocation){
		console.log("NAVIGATION");
		navigator.geolocation.getCurrentPosition(showPosition, function(error){
			alert("Error in obtaining position - Code: " + error.code + ", Message: " + error.message);
		}, {maximumAge:60000, timeout:5000, enableHighAccuracy:true});
	}
}

function showPosition(position){
	console.log("IN SHOWPOSITION");
	var appElement = document.querySelector('[ng-controller=placeTableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.show['place'] = false;
		});
	}
	else{
		$scope.show['place'] = false;
	}

	console.log("Here1");

	document.getElementById('progressBar').style.display = "block";
	var query = document.getElementById('textFieldTopBar').value.replace(/ /g, "+");
	
	if(query=="" || query==null){
		alert("Error: Empty input field!");
	}
	else{
		//Make AJAX call
		console.log("Here2");
		$.ajax({
			url: 'http://samp-app.us-west-2.elasticbeanstalk.com',
			data: {
				'query': query,
				'type': 'place',
				'latitude': position.coords.latitude,
				'longitude': position.coords.longitude
			},
			type: 'GET',
			success: function(response){
				if(response.status){
					var obj = JSON.parse(response.data);

					if(!$scope.$$phase){
						$scope.$apply(function(){
							$scope.show['place'] = true;
							$scope.entries = obj;
							$scope.toggleArr = [];

							if(obj.paging.previous){
								$scope.prevLink = obj.paging.previous;
								$scope.showPrevButton = true;
							}

							if(obj.paging.next){
								$scope.nextLink = obj.paging.next;
								$scope.showNextButton = true;
							}

							for(var i = 0; i<obj.data.length; i++){
								$scope.toggleArr[obj.data[i].id] = false;
								if(localStorage.getItem(obj.data[i].id)){
									$scope.toggleArr[obj.data[i].id] = true;
								}
							}
						});
					}
					else{
						$scope.show['place'] = true;
						$scope.entries = obj;
						$scope.toggleArr = [];

						if(obj.paging.previous){
							$scope.prevLink = obj.paging.previous;
							$scope.showPrevButton = true;
						}

						if(obj.paging.next){
							$scope.nextLink = obj.paging.next;
							$scope.showNextButton = true;
						}

						for(var i = 0; i<obj.data.length; i++){
							$scope.toggleArr[obj.data[i].id] = false;
							if(localStorage.getItem(obj.data[i].id)){
								$scope.toggleArr[obj.data[i].id] = true;
							}
						}
					}
					console.log("Over");
					document.getElementById('progressBar').style.display = "none";
				}
				else{
					//Give error message
					alert('Error: Query could not be processed successfully!');
					document.getElementById('progressBar').style.display = "none";
				}
			},
			error: function(response){
				//Parse error
				alert('Error: AJAX error!');
				document.getElementById('progressBar').style.display = "none";
			}
		});
	}
}

$('#clearButton').click(function(e){
	e.preventDefault();
	invalidateRightSlides();
	document.getElementById('progressBar').style.display = "none";
	document.getElementById('textFieldTopBar').value = "";
	var appElement = document.querySelector('[ng-controller=userTableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.entries = null;
			$scope.show['user'] = false;
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.entries = null;
		$scope.show['user'] = false;
		$scope.showAlbsPosts = false;
	}

	appElement = document.querySelector('[ng-controller=pageTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.entries = null;
			$scope.show['page'] = false;
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.entries = null;
		$scope.show['page'] = false;
		$scope.showAlbsPosts = false;
	}

	appElement = document.querySelector('[ng-controller=eventTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.entries = null;
			$scope.show['event'] = false;
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.entries = null;
		$scope.show['event'] = false;
		$scope.showAlbsPosts = false;
	}

	appElement = document.querySelector('[ng-controller=placeTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.entries = null;
			$scope.show['place'] = false;
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.entries = null;
		$scope.show['place'] = false;
		$scope.showAlbsPosts = false;
	}

	appElement = document.querySelector('[ng-controller=groupTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.entries = null;
			$scope.show['group'] = false;
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.entries = null;
		$scope.show['group'] = false;
		$scope.showAlbsPosts = false;
	}

	appElement = document.querySelector('[ng-controller=favoriteTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.showAlbsPosts = false;
		});
	}
	else{
		$scope.showAlbsPosts = false;
	}

	return false;
});

function fetchFavorites(){
	invalidateRightSlides();
	var appElement = document.querySelector('[ng-controller=favoriteTableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			if(localStorage.length!=0){
				$scope.updateFavorites();
				$scope.showFav = true;
			}
			else{
				alert("You have no favorites!");
				$scope.showFav = false;
			}
		});
	}
	else{
		if(localStorage.length!=0){
			$scope.updateFavorites();
			$scope.showFav = true;
		}
		else{
			alert("You have no favorites!");
			$scope.showFav = false;
		}
	}
}

function invalidateRightSlides(){
	var appElement = document.querySelector('[ng-controller=userTableGenerator]');
	var $scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}

	appElement = document.querySelector('[ng-controller=pageTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}

	appElement = document.querySelector('[ng-controller=eventTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}

	appElement = document.querySelector('[ng-controller=placeTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}

	appElement = document.querySelector('[ng-controller=groupTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}

	appElement = document.querySelector('[ng-controller=favoriteTableGenerator]');
	$scope = angular.element(appElement).scope();

	if(!$scope.$$phase){
		$scope.$apply(function(){
			$scope.slideFromRight = false;
		});
	}
	else{
		$scope.slideFromRight = false;
	}
}

function fetchAlbumsAndPostsFromServer(entityId, type, callback){
	document.getElementById('progressBarPosts-'+type).style.display = "block";
	document.getElementById('progressBarAlbums-'+type).style.display = "block";

	var appElement, $scope;
	
	appElement = document.querySelector('[ng-controller='+type+'TableGenerator]');
	$scope = angular.element(appElement).scope();

	if(type=='event'){
		document.getElementById('progressBarPosts-'+type).style.display = "none";
		document.getElementById('progressBarAlbums-'+type).style.display = "none";
		if(!$scope.$$phase){
			$scope.$apply(function(){
				$scope.post_data = false;
				$scope.accordion = false;
				$scope.no_data_post = true;
				$scope.no_data_album = true;
			});
		}
		else{
			$scope.post_data = false;
			$scope.accordion = false;
			$scope.no_data_post = true;
			$scope.no_data_album = true;
		}
		callback(null, null, null, null);
	}
	else{
		if(!$scope.$$phase){
			$scope.$apply(function(){
				$scope.toggleArr[""] = false;
			});
		}
		else{
			$scope.toggleArr[""] = false;
		}
			
		//Make AJAX call
		console.log("Here3");
		$.ajax({
			url: 'http://samp-app.us-west-2.elasticbeanstalk.com',
			data: {
				'id': entityId
			},
			type: 'GET',
			success: function(response){
				if(response.status){
					var obj = JSON.parse(response.data);
					$scope.id = obj.id;
						
					if(obj.albums){
						console.log("1");
						if(obj.albums.data.length!=0){
							console.log("2");
							document.getElementById('progressBarAlbums-'+type).style.display = "none";
							if(!$scope.$$phase){
								$scope.$apply(function(){
									$scope.albumEntries = obj.albums;
									$scope.no_data_album = false;
									$scope.accordion = true;
									console.log("2a " + $scope.albumEntries.data[0].name);
								});
							}
							else{
								console.log("2b");
								$scope.albumEntries = obj.albums;
								$scope.no_data_album = false;
								$scope.accordion = true;
							}
						}
					}
					else{
						console.log("3");
						document.getElementById('progressBarAlbums-'+type).style.display = "none";
						if(!$scope.$$phase){
							$scope.$apply(function(){
								console.log("3a");
								$scope.albumEntries = null;
								$scope.accordion = false;
								$scope.no_data_album = true;
							});
						}
						else{
							console.log("3b");
							$scope.albumEntries = null;
							$scope.accordion = false;
							$scope.no_data_album = true;
						}	
					}

					if(obj.posts){

						for(var i = 0; i<obj.posts.data.length; i+=1){
							if(!obj.posts.data[i].message && !obj.posts.data[i].story){
								obj.posts.data.splice(i,1);
							}
						}

						if(obj.posts.data.length!=0){
							document.getElementById('progressBarPosts-'+type).style.display = "none";
							if(!$scope.$$phase){
								$scope.$apply(function(){
									$scope.postEntries = obj.posts;
									$scope.no_data_post = false;
									$scope.post_data = true;
								});
							}
							else{
								$scope.postEntries = obj.posts;
								$scope.no_data_post = false;
								$scope.post_data = true;
							}
						}
					}
					else{
						document.getElementById('progressBarPosts-'+type).style.display = "none";
						if(!$scope.$$phase){
							$scope.$apply(function(){
								$scope.albumEntries = null;
								$scope.post_data = false;
								$scope.no_data_post = true;
							});
						}
						else{
							$scope.albumEntries = null;
							$scope.post_data = false;
							$scope.no_data_post = true;
						}
					}
					callback(obj.albums, obj.posts, obj.name, obj.picture.data.url);
					console.log("Over");
				}
				else{
					//Give error message
					alert('Error: Query could not be processed successfully!');
					document.getElementById('progressBarPosts-'+type).style.display = "none";
					document.getElementById('progressBarAlbums-'+type).style.display = "none";
					if(!$scope.$$phase){
						$scope.$apply(function(){
							$scope.post_data = false;
							$scope.accordion = false;
							$scope.no_data_post = true;
							$scope.no_data_album = true;
						});
					}
					else{
						$scope.post_data = false;
						$scope.accordion = false;
						$scope.no_data_post = true;
						$scope.no_data_album = true;
					}

					callback(null, null, null, null);
				}
			},
			error: function(response){
				//Parse error
				alert('Error: Server error!');
				document.getElementById('progressBarPosts-'+type).style.display = "none";
				document.getElementById('progressBarAlbums-'+type).style.display = "none";
				if(!$scope.$$phase){
					$scope.$apply(function(){
						$scope.post_data = false;
						$scope.accordion = false;
						$scope.no_data_post = true;
						$scope.no_data_album = true;
					});
				}
				else{
					$scope.post_data = false;
					$scope.accordion = false;
					$scope.no_data_post = true;
					$scope.no_data_album = true;
				}

				callback(null, null, null, null);
			}
		});
	}
}