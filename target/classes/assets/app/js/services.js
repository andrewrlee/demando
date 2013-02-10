'use strict';

/* Services */
var services = angular.module('myApp.services', []);

services.value('version', '0.1');

services.factory('environmentService', [ '$resource', function($resource) {
	var environmentService = {};
	
	environmentService.list = function(callback) {
		var url = "/service/environment/"
		$resource(url).query(function(data) {
			callback(data);
		});
	}

	environmentService.get = function($scope, id) {
		var url = "/service/environment/" + id;
		$resource(url).get(function(data) {
			$scope.environment = data;
		});
	}

	environmentService.update = function(data) {
		var url = "/service/environment/" + data.id;
		$resource(url).save(data);
	}
	return environmentService;
} ]);