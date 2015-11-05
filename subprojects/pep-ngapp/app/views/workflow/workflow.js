app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/workflow', {
            controller: 'ResourcesListCtrl',
            templateUrl: 'views/workflow/list.html'
        });
}]);