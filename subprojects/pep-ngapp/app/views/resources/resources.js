app.controller('ResourcesListCtrl', ['$scope', '$http', '$q', '$timeout', function($scope, $http, $q, $timeout) {

    $scope.selected = [];

    $scope.filter = {
        show: false
    };

    $scope.query = {
        order: 'name',
        limit: 10,
        page: 1,
        label: {
            text: '每页行数',
            of: '/'
        }
    };

    $scope.resources = { };

    $http.get('/data/resources.json').success(function(data) {
        $scope.resources.count = data.length;
        $scope.resources.data = data;
    });

    $scope.onpagechange = function(page, limit) {
        var deferred = $q.defer();

        $timeout(function () {
            deferred.resolve();
        }, 2000);

        return deferred.promise;
    };

    $scope.onorderchange = function(order) {
        var deferred = $q.defer();

        $timeout(function () {
            deferred.resolve();
        }, 2000);

        return deferred.promise;
    };
}]);