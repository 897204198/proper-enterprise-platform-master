var app = angular.module('pep', ['ngMaterial']);

app.controller('AppCtrl', ['$scope', '$mdSidenav', '$http', function($scope, $mdSidenav, $http) {
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.resources = [];

    $scope.showUrl = function(url) {
        console.log(url);
    };

    $http.get('/data/resources.json').success(function(data) {
        $scope.resources = data;
    });

}]);