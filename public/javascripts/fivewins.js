var fiveWinsApp = angular.module('fiveWinsApp', [ 'ngRoute' ]);

fiveWinsApp.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/assets/htmls/start.html',
		controller : 'FiveWinsStartCtrl'
	}).when('/game/', {
		templateUrl : '/assets/htmls/game.html',
		controller : 'FiveWinsGameCtrl'
	}).when('/about', {
		templateUrl : '/assets/htmls/about.html',
		controller : 'FiveWinsAboutCtrl'
	}).when('/login', {
		templateUrl : '/assets/htmls/login.html',
		controller : 'FiveWinsStartCtrl'
	}).when('/test', {
		templateUrl : '/assets/htmls/test.html',
		controller : 'FiveWinsStartCtrl'
	}).otherwise({
		redirectTo : '/'
	});
});

fiveWinsApp.controller('FiveWinsStartCtrl', function($scope, $http) {
	/*
	 * $http.get('countries.json').success(function(data) { $scope.countries =
	 * data; });
	 */
});

fiveWinsApp.controller('FiveWinsAboutCtrl', function($scope, $http) {
	/*
	 * $http.get('countries.json').success(function(data) { $scope.countries =
	 * data; });
	 */
});

fiveWinsApp.controller('FiveWinsGameCtrl', function($scope, $routeParams,
		$compile, $http) {
	// variables
	$scope.fieldSize = 8;

	$scope.field = [];

	// Initial field
	for (i = 0; i < $scope.fieldSize; i++) {
		$scope.field[i] = [];
		for (j = 0; j < $scope.fieldSize; j++) {
			$scope.field[i][j] = " ";
		}
	}

	$scope.isGameStarted = false;

	$scope.numberOfTurns = 0;

	$scope.currentPlayer = 'X';
	
	$scope.playerId = "";

	$scope.winner = '';
	$scope.draw = false;
	
	// functions
	$scope.resizeField = function() {
		newArray = [];
		for (i = 0; i < $scope.fieldSize; i++) {
			newArray[i] = [];
			for (j = 0; j < $scope.fieldSize; j++) {
				newArray[i][j] = " ";
			}
		}

		$scope.field = newArray;
	};

	$scope.addEventListener = function() {
		console.log('isGameStarted = true');
		console.log($('#gameType').val());
		$scope.isGameStarted = true;
		$("#config").toggle('slow');
		// $('#config').hide();
		$('#gameOptions').show('slow');
		// ajax call to start game
		if ($('#gameType').val() == "PVP") {
			$.post("/game/play/" + $scope.fieldSize, function(data) {
				console.log("Initial Game(PVP).");
				$scope.initWebsocket();
			});
		} else if ($('#gameType').val() == "NPC") {
			$.post("/game/play/" + $scope.fieldSize + "/NPC/O", function(data) {
				console.log("Initial Game(NPC).");
				$scope.initWebsocket();
			});
		}
	};
	
	$scope.joinOnlineGame = function() {
			
		console.log($('#gameType').val());
		$("#config").toggle('slow');
		// $('#config').hide();
		$('#gameOptions').show('slow');
		$scope.fieldSize = 8;
		$scope.resizeField();
		// ajax call to start game
		$.post("/game/playOnline", function(data) {
			console.log("Initial Game(PVPonline).");
			$scope.playerId = data.playerId;
			console.log("playerId"+data.playerId);
			// start game instantly for Player O
			if($scope.playerId == 'O'){
				$scope.isGameStarted = true;
				console.log('isGameStarted = true');
			}else{
				document.getElementById('field').style.display = 'none';
			}
			$scope.$apply();
		});
		
		// without time out socket init is to fast and GameFieldObserver is
		// initialised with old gameId
		setTimeout(function(){
			$scope.initWebsocket();
		}, 2000);	
	}

	$scope.pressed = function($event) {
		if ($scope.isGameStarted) {
			if($scope.playerId != $scope.currentPlayer && $scope.playerId !== ""){
				return;
			}
			console.log($event.target);
			row = $($event.target).parent().attr("id").split("_")[1];
			cell = $($event.target).attr("id");
			// delete eventlisener from target and :hover

			// get all gameinformation
			$.post("/setCell/" + row + "/" + cell, function(data) {
			});

		}
	};

	$scope.endResult = function() {
		$scope.isGameStarted = false;
		$("#config").empty();
		$("#config").load('/assets/htmls/winner.html', function() {
			$("#config").show('slow');
			$("#winnerPlayer").text($scope.winner);
		});

	};

	$scope.endResultDraw = function() {
		$scope.isGameStarted = false;
		$("#config").empty();
		$("#config").load('/assets/htmls/draw.html', function() {
			$("#config").show('slow');
		});

	};

	$scope.initWebsocket = function() {

		connect();

		function connect() {
			var wsproto = 'ws://';
			if (window.location.protocol === 'https:') {
				wsproto = 'wss://';
			}
			var socketUrl = wsproto + location.host + '/socket';
			var socket = new WebSocket(socketUrl);

			console.log('Socket Status: ' + socket.readyState + ' (ready)');

			socket.onopen = function() {
				console.log('Socket Status: ' + socket.readyState + ' (open)');
			};

			socket.onmessage = function(msg) {
				var data = JSON.parse(msg.data);
				
				if(data.started == "true"){
					$scope.isGameStarted = 'true';
					console.log('isGameStarted = true');
					document.getElementById('field').style.display = 'inline-block';
					return;
				}
				
				console.log('Socket: onmessage()');
				// update Gamefield
				
				$scope.field = JSON.parse(data.gameField);
				$scope.numberOfTurns++;
				$scope.currentPlayer = data.playerSign;
				$scope.winner = data.winner;
				$scope.draw = data.isDraw;
				$scope.$apply();
				if (data.isWon == "true") {
					$scope.endResult();
					return;
				}
				if (data.isDraw == "true") {
					$scope.endResultDraw();
					return;
				}
				console.log(data.status);
				console.log(data);
			};

			socket.onclose = function() {
				console
						.log('Socket Status: ' + socket.readyState
								+ ' (Closed)');
				socket.close();
			};

			function send() {
				/*
				 * var grid = ""; socket.send(grid); console.log('Sent grid ');
				 */
			}
		}
		// End connect
	};
	
	$scope.signinCallback = function (authResult) {
		if (authResult['status']['signed_in']) {
			// Update the app to reflect a signed in user
			// Hide the sign-in button now that the user is authorized, for example:
			document.getElementById('signinButton').setAttribute('style',
					'display: none');
			document.getElementById('signoutButton').setAttribute('style',
					'display: normal');
			$scope.joinOnlineGame();
		} else {
			if (authResult['error'] == "user_signed_out") {
				document.getElementById('signinButton').setAttribute('style',
						'display: normal');
				document.getElementById('signoutButton').setAttribute('style',
						'display: none');
			}
			// Possible error values:
			//   "user_signed_out" - User is signed-out
			//   "access_denied" - User denied access to your app
			//   "immediate_failed" - Could not automatically log in the user
			console.log('Sign-in state: ' + authResult['error']);
		}
	};
	
	$scope.logout = function() {
		gapi.auth.signOut();
	};

});
