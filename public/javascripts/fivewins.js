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

	$scope.field = [ [ " ", " ", " " ], [ " ", " ", " " ], [ " ", " ", " " ] ];
	
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
			// delete eventlisener from target and :hover effekt
			
			
			// get all gameinformation
			$.post("/setCell/" + row + "/" + cell, function(data) {
				console.log("Get game information.");
				var msg = JSON.parse(data);
				$scope.numberOfTurns++;
				$scope.currentPlayer = msg.playerSign;
				$scope.winner = msg.winner;
				$scope.draw = msg.isDraw;
				$scope.$apply();
				if (msg.isWon == "true") {
					$scope.endResult();
					return;
				}
			});
			
			//get changed gamefield
			$.post("/game/field", function(data) {
				console.log("Get gamefield.");
				$scope.field = JSON.parse(data);
				$scope.$apply();
			});
		}
	};
	
	$scope.endResult = function() {
		$( "#config" ).empty();
//		$.get("/assets/htmls/test.html", function(data){
//		    $("#config").children().html(data);
//		});
//		$( "#config" ).append('output');
		$('#config').load('/assets/htmls/test.html', function() {
			$( "#config" ).show('slow');
		});
		
	};
	
	$scope.initWebsocket = function() {

		connect();

		function connect() {
			var socket = new WebSocket("ws://localhost:9000/socket");

			//message('Socket Status: ' + socket.readyState + ' (ready)');

			socket.onopen = function() {
				alert("Open Socket");
				//message('Socket Status: ' + socket.readyState + ' (open)');
			};

			socket.onmessage = function(msg) {
				alert("Update recieved");
				var data = msg.data;
				updateField(data);
			};

			socket.onclose = function() {
				//message('Socket Status: ' + socket.readyState + ' (Closed)');
				socket.close();
			};

			function send() {
				var grid = "";
				socket.send(grid);
				//message('Sent grid ');
			}
		}
		// End connect
	};

});

