var fiveWinsApp = angular.module('fiveWinsApp', [ 'ngRoute' ]);

fiveWinsApp.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/assets/htmls/start.html',
		controller : 'FiveWinsStartCtrl'
	}).when('/game', {
		templateUrl : '/assets/htmls/game.html',
		controller : 'FiveWinsGameCtrl'
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

fiveWinsApp.controller('FiveWinsGameCtrl', function($scope, $routeParams,
		$compile, $http) {
	// variables
	$scope.fieldSize = 15;
	
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
		$scope.isGameStarted = true;
		$( "#config" ).toggle('slow');
//		$('#config').hide();
		$('#gameOptions').show('slow');
		// ajax call to start game
		$.post("/game/play/" + $scope.fieldSize, function(data) {
			console.log("Initial Game.");
			$scope.initWebsocket();
		});
	};

	$scope.pressed = function($event) {
		if ($scope.isGameStarted) {
			console.log($event.target);
			row = $($event.target).parent().attr("id").split("_")[1];
			cell = $($event.target).attr("id");
			// delete eventlisener from target and :hover 
			
			
			// get all gameinformation
			$.post("/setCell/" + row + "/" + cell, function(data) { });
			
		}
	};
	
	$scope.endResult = function() {
		$scope.isGameStarted = false;
		$( "#config" ).empty();
		$('#config').load('/assets/htmls/test.html', function() {
			$( "#config" ).show('slow');
		});
		
	};
	
	$scope.initWebsocket = function() {

		connect();

		function connect() {
			var socket = new WebSocket("ws://localhost:9000/socket");

			console.log('Socket Status: ' + socket.readyState + ' (ready)');

			socket.onopen = function() {
				console.log('Socket Status: ' + socket.readyState + ' (open)');
			};

			socket.onmessage = function(msg) {
				console.log('Socket: onmessage()');
				// update Gamefield
				var data = JSON.parse(msg.data);
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
				console.log(data.status);
				console.log(data);
			};

			socket.onclose = function() {
				console.log('Socket Status: ' + socket.readyState + ' (Closed)');
				socket.close();
			};

			function send() {
				/*var grid = "";
				socket.send(grid);
				console.log('Sent grid ');*/
			}
		}
		// End connect
	};

});

