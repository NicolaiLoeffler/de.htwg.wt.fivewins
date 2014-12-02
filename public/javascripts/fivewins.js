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
		$compile) {
	// console.log($routeParams);
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
		/*
		 * for(i=0; i < $scope.fieldSize;i++) {
		 * $('#row_'+i).children().each(function () { //console.log($(this));
		 * $(this).attr('ng-click','pressed()'); $compile($(this))($scope);
		 * 
		 * console.log('test'); }); console.log('#row_'+i); }
		 */
		console.log('isGameStarted = true');
		$scope.isGameStarted = true;
		$('#config').hide();
		$('#gameOptions').show()
	};

	$scope.pressed = function($event) {
		if ($scope.isGameStarted) {
			console.log($event.target);
			row = $($event.target).parent().attr("id").split("_")[1];
			cell = $($event.target).attr("id");
			console.log(row);
			console.log(cell);

		}
	};

});