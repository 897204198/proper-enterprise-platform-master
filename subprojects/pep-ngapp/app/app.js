var app = angular.module('pep', ['ngMaterial']);

app.controller('AppCtrl', ['$scope', '$mdSidenav', '$http', function($scope, $mdSidenav, $http) {
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.title;
    $scope.resources = [];

    $scope.clickMenu = function(res) {
        $scope.title = res.name;
        console.log(res.url);
    };

    $http.get('/data/resources.json').success(function(data) {
        $scope.resources = data;
    });

}]);