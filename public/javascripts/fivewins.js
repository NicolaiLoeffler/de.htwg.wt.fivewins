var fiveWinsApp = angular.module('fiveWinsApp', [ 'ngRoute' ]);

fiveWinsApp.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '/assets/htmls/start.html',
		controller : 'FiveWinsStartCtrl'
	}).when('/game', {
		templateUrl : '/assets/htmls/game.html',
		controller : 'FiveWinsGameCtrl'
	}).when('/test', {
		templateUrl : 'test.html',
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
		$('#config').hide();
		$('#gameOptions').show();
		// ajax call to start game
		$.post("/game/play/" + $scope.fieldSize, function(data) {
			console.log("Initial Game.");
		});
	};

	$scope.pressed = function($event) {
		if ($scope.isGameStarted) {
			console.log($event.target);
			row = $($event.target).parent().attr("id").split("_")[1];
			cell = $($event.target).attr("id");
			console.log(row);
			console.log(cell);
			
			// get all gameinformation
			$.post("/setCell/" + row + "/" + cell, function(data) {
				console.log("Ajax call 1.");
				var msg1 = JSON.parse(data);
				console.log(msg1);
			});
			
			//get changed gamefield
			$.post("/game/field", function(data) {
				console.log("Get gamefield.");
				$scope.field = JSON.parse(data);
				$scope.$apply();
			});

		}
	};

});