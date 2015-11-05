var app = angular.module('pep', ['ngRoute', 'ngMaterial', 'md.data.table', 'ares']);

app.controller('AppCtrl', ['$scope', '$mdSidenav', '$http', '$timeout', '$location', '$document', function($scope, $mdSidenav, $http, $timeout, $location, $document) {
    $scope.title = '';
    $scope.resources = [];

    $scope.toggleSidenav = function(id) {
        $mdSidenav(id).toggle();
    };

    $scope.clickMenu = function(res) {
        $scope.title = res.name;
        $location.url(res.url);
        $timeout(function() {
            $scope.toggleSidenav('menu');
        }, 100);
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
    });
}]);