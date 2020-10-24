var gameModule = angular.module('gameModule', []);

gameModule.controller('lobbyController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {

        rootScope.stompClient = null;

        scope.createNewGame = function () {

            http.post("/game/create", {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                rootScope.gameId = data.id;
                location.path('/game/' + rootScope.gameId);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
        };

        rootScope.connectLobby = function connect() {
            var socket = new SockJS('/socket');
            scope.stompClient = Stomp.over(socket);
            scope.stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                scope.stompClient.subscribe('/send-update/lobby', rootScope.reloadPlayerGames);
                scope.stompClient.subscribe('/send-update/lobby', rootScope.reloadGamesToJoin);
            });
        };

        scope.connectLobby();

    }
]);


gameModule.controller('gamesToJoinController', ['$rootScope', '$scope', '$http', '$location',
    function (rootScope, scope, http, location) {
        rootScope.reloadGamesToJoin = function () {
            scope.gamesToJoin = [];
            http.get('/game/list').success(function (data) {
                scope.gamesToJoin = data;
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });


            scope.joinGame = function (id) {
                var requestUrl = "/game/join?id=" + id;
                http.post(requestUrl, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    rootScope.gameId = data.id;
                    location.path('/game/' + data.id);
                }).error(function (data, status, headers, config) {
                    location.path('/player/panel');
                });
            }
        };

        rootScope.reloadGamesToJoin();
    }]);


gameModule.controller('playerGamesController', ['$rootScope', '$scope', '$http', '$location', '$routeParams',
    function (rootScope, scope, http, location, routeParams) {
        rootScope.reloadPlayerGames  =function () {

            scope.playerGames = [];

            http.get('/game/player/list').success(function (data) {
                scope.playerGames = data;
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });

            scope.loadGame = function (id) {
                console.log(id);
                rootScope.gameId = id;
                http.get('/game/' + id).success(function (data) {
                    location.path('/game/' + id);
                }).error(function (data, status, headers, config) {
                    location.path('/player/panel');
                });
            }
        };

        rootScope.reloadPlayerGames();

    }]);


gameModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        rootScope.stompClient = null;

        rootScope.reload = function getData() {

            http.get('/play/board').success(function (data) {
                scope.data = data
                scope.gameBoard = [];
                data.pits.forEach(function (pit) {
                    scope.gameBoard[pit.position] = pit.numberOfStones;
                })
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/play/turn').success(function (data) {
                scope.gameTurn = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/play/state').success(function (data) {
                scope.gameState = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/play/score').success(function (data) {
                scope.gameScore = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
        };

        rootScope.reload();

        rootScope.connectBoard = function connect() {
            var socket = new SockJS('/socket');
            scope.stompClient = Stomp.over(socket);
            scope.stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                scope.stompClient.subscribe('/send-update/position/' + rootScope.gameId, scope.reload);
                scope.stompClient.subscribe('/send-update/join/' + rootScope.gameId, scope.reload);
            }, function (error) {
                alert(error.headers.message);
            });
        };

        scope.connectBoard();

        scope.move = function (id) {
            http.post('/play/move/' + id).success(function (data) {
                scope.data = data
                scope.reload();
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Move Failed";
            });
        };

    }
]);
var mancalaApp = angular.module('mancalaApp', ['ngRoute', 'gameModule']);




mancalaApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/player', {
        templateUrl: 'templates/lobby.html',
        controller: 'lobbyController'
    }).
    when('/game/:id', {
        templateUrl: 'templates/game.html',
        controller: 'gameController'
    }).

    otherwise({
        redirectTo: '/player'
    });
}]);