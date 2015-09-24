var app = angular.module('pep', ['ngRoute', 'ngMaterial', 'md.data.table']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/resources/list', {
            controller: 'ResourcesListCtrl',
            templateUrl: 'views/resources/list.html'
        });
}]);

app.controller('AppCtrl', ['$scope', '$mdSidenav', '$http', '$timeout', '$location', '$document', function($scope, $mdSidenav, $http, $timeout, $location, $document) {
    $scope.toggleSidenav = function(menuId) {
        $mdSidenav(menuId).toggle();
    };

    $scope.title;
    $scope.resources = [];

    $scope.clickMenu = function(res) {
        $scope.title = res.name;
        $location.url(res.url);
        $timeout(function() {
            $scope.toggleSidenav('menu');
        }, 500);
    };

    $http.get('/data/resources.json').success(function(data) {
        $scope.resources = data;
    });

    $document.bind('keypress', function(event) {
        // key code 113 is 'q'
        var code = event.keyCode || event.charCode;
        var actEleType = document.activeElement.type;
        if ('text' != actEleType && code == 113) {
            $scope.toggleSidenav('menu');
        }
        console.log(actEleType);
    });
}]);