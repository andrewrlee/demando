'use strict';

/* Controllers */

function MainController($scope) {
}
MainController.$inject = ['$scope'];

function questionCtrl($rootScope,  $scope, $location, $routeParams, questionService) {
	questionService.get($scope, $routeParams.id);
	$scope.id = $routeParams.id

	$scope.save = function(){
		$scope.question.$save(function(data) {
			$rootScope.$broadcast("model-update");
		});
	} 
	$scope.delete = function(){
		$scope.question.$delete();
		$location.path("/main");
		$rootScope.$broadcast("model-update");
	} 
}
questionCtrl.$inject = ['$rootScope', '$scope', '$location', '$routeParams', 'questionService'];

function NavController($rootScope, $scope, $http, $routeParams, questionService) {
	questionService.list(function(data){$scope.questions = data});
	$scope.$on("model-update", function(){
		questionService.list(function(data){
				$scope.questions = data
				console.log("update called", $scope)
			});
	});
}
NavController.$inject = [ '$rootScope','$scope', '$http', '$routeParams', 'questionService' ];
