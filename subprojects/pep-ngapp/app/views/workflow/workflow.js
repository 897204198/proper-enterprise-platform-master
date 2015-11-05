var workflowRoute = function($routeProvider) {
    $routeProvider
        .when('/workflow', {
            controller: 'WorkflowCtrl',
            templateUrl: 'views/workflow/list.html'
        });
};

app.config(['$routeProvider', workflowRoute]);

var workflowCtrl = function($scope, $http) {
    $scope.query = {
        order: 'name',
        limit: 10,
        page: 1,
        label: {
            text: '每页行数',
            of: '/'
        }
    };

    $scope.models = { };
    $http.get('/data/models.json').success(function(data) {
        $scope.models = data;
    });
};

app.controller('WorkflowCtrl', ['$scope', '$http', workflowCtrl]);