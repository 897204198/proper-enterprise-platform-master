app.controller('ResourcesListCtrl', ['$scope', '$http', '$q', '$timeout', '$mdDialog', function($scope, $http, $q, $timeout, $mdDialog) {

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

    $scope.addItem = function(ev) {
        $mdDialog.show({
            controller: DialogCtrl,
            templateUrl: 'views/resources/new.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            escapeToClose: false
        }).then(function(data) {
            if ($scope.resources.count && $scope.resources.data) {
                $scope.resources.data.push(data);
                $scope.resources.count += 1;
            }
        });
    };

}]);

function DialogCtrl($scope, $mdDialog) {
    $scope.hide = function() {
        $mdDialog.hide();
    };
    $scope.cancel = function() {
        $mdDialog.cancel();
    };
    $scope.save = function() {
        $mdDialog.hide($scope.res);
    };
}