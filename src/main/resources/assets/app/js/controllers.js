'use strict';

/* Controllers */

function MainController($scope) {
}
MainController.$inject = ['$scope'];

function EnvironmentCtrl($rootScope,  $scope, $location, $routeParams, environmentService) {
	environmentService.get($scope, $routeParams.id);
	$scope.id = $routeParams.id

	$scope.save = function(){
		$scope.environment.$save(function(data) {
			$rootScope.$broadcast("model-update");
		});
	} 
	$scope.delete = function(){
		$scope.environment.$delete();
		$location.path("/main");
		$rootScope.$broadcast("model-update");
	} 
}
EnvironmentCtrl.$inject = ['$rootScope', '$scope', '$location', '$routeParams', 'environmentService'];

function NavController($rootScope, $scope, $http, $routeParams, environmentService) {
	environmentService.list(function(data){$scope.environments = data});
	$scope.$on("model-update", function(){
		environmentService.list(function(data){
				$scope.environments = data
				console.log("update called", $scope)
			});
	});
}
NavController.$inject = [ '$rootScope','$scope', '$http', '$routeParams', 'environmentService' ];
