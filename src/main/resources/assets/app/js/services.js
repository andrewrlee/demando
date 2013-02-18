'use strict';

/* Services */
var services = angular.module('myApp.services', []);

services.value('version', '0.1');

services.factory('questionService', [ '$resource', function($resource) {
	var questionService = {};
	
	questionService.list = function(callback) {
		var url = "/service/question/"
		$resource(url).query(function(data) {
			callback(data);
		});
	}

	questionService.get = function($scope, id) {
		var url = "/service/question/" + id;
		$resource(url).get(function(data) {
			$scope.question = data;
		});
	}

	questionService.update = function(data) {
		var url = "/service/question/" + data.id;
		$resource(url).save(data);
	}
	return questionService;
} ]);