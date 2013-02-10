'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'ngSanitize', 'ngResource']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/main', {templateUrl: 'partials/main.html', controller: MainController});
    $routeProvider.when('/new', {templateUrl: 'partials/new.html', controller: MainController});
    $routeProvider.when('/environment/:id', {templateUrl: 'partials/environment.html', controller: EnvironmentCtrl});
    $routeProvider.otherwise({redirectTo: '/main'});
  }]);
