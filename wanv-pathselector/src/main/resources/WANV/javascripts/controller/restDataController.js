function customersController($scope,$http) {
    $http.get("/pathselector/setup/connector")
        .success(function (response) {
            $scope.connectors = response;
        });
}

function routeController($scope,$http){

    $http.get("/pathselector/routing/info/full")
        .success(function (response) {
            $scope.route = response;
        });
}

function flowController($scope,$http) {

    $http.get("/pathselector/flow/solution")
        .success(function (response) {
            $scope.flows = response;
        });
}