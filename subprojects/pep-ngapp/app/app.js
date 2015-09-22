var app = angular.module('pep', ['ngRoute', 'ngMaterial']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/resources/list', {
            controller: 'ResourcesListCtrl',
            templateUrl: 'views/resources/list.html'
        });
}]);

app.controller('AppCtrl', ['$scope', '$mdSidenav', '$http', function($scope, $mdSidenav, $http) {
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.title;
    $scope.resources = [];

    $scope.clickMenu = function(res) {
        $scope.title = res.name;
    };

    $http.get('/data/resources.json').success(function(data) {
        $scope.resources = data;
    });

}]);