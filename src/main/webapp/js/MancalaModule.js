var mancalaModule = angular.module('mancalaModule', []);


mancalaModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        rootScope.stompClient = null;

        rootScope.reload = function getData() {

            http.get('/game/current-board').success(function (data) {
                scope.data = data
                scope.gameBoard = [];
                data.pits.forEach(function (pit) {
                    scope.gameBoard[pit.position] = pit.stoneCount;
                })
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/game/player-in-action').success(function (data) {
                scope.gameTurn = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/game/current-state').success(function (data) {
                scope.gameState = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });

        };

        rootScope.reload();

        rootScope.connectBoard = function connect() {
            var socket = new SockJS('/socket');
            scope.stompClient = Stomp.over(socket);
            scope.stompClient.connect({}, function (frame) {
                scope.stompClient.subscribe('/send-update/position/' + rootScope.gameId, scope.reload);
                scope.stompClient.subscribe('/send-update/join/' + rootScope.gameId, scope.reload);
            }, function (error) {
            });
        };

        scope.connectBoard();

        scope.move = function (id) {
            http.post('/game/make-move?position='+id).success(function (data) {
                scope.data = data
                scope.reload();
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Move Failed";
            });
        };

    }
]);

mancalaModule.controller('lobbyController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {

        rootScope.stompClient = null;

        scope.createNewGame = function () {

            http.post("/lobby/game/prepare", {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {
                rootScope.gameId = data.id;
                location.path('/game/' + rootScope.gameId);
            })
        };

        rootScope.connectLobby = function connect() {
            var socket = new SockJS('/socket');
            scope.stompClient = Stomp.over(socket);
            scope.stompClient.connect({}, function (frame) {
                scope.stompClient.subscribe('/send-update/lobby', rootScope.reloadPlayerGames);
                scope.stompClient.subscribe('/send-update/lobby', rootScope.reloadGamesToJoin);
            });
        };

        scope.connectLobby();

    }
]);


mancalaModule.controller('gamesToJoinController', ['$rootScope', '$scope', '$http', '$location',
    function (rootScope, scope, http, location) {
        rootScope.reloadGamesToJoin = function () {
            scope.gamesToJoin = [];
            http.get('/lobby/to-be-joined/game/list').success(function (data) {
                scope.gamesToJoin = data;
            })


            scope.joinGame = function (id) {
                var requestUrl = "/lobby/join/game?id=" + id;
                http.post(requestUrl, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    rootScope.gameId = data.id;
                    location.path('/game/' + data.id);
                })
            }
        };

        rootScope.reloadGamesToJoin();
    }]);


mancalaModule.controller('playerGamesController', ['$rootScope', '$scope', '$http', '$location', '$routeParams',
    function (rootScope, scope, http, location, routeParams) {
        rootScope.reloadPlayerGames  =function () {

            scope.playerGames = [];

            http.get('/lobby/player/own-games/list').success(function (data) {
                scope.playerGames = data;
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });

            scope.loadGame = function (id) {
                rootScope.gameId = id;
                http.get('/game/' + id).success(function (data) {
                    location.path('/game/' + id);
                })
            }
        };

        rootScope.reloadPlayerGames();

    }]);

var mancalaApp = angular.module('mancalaApp', ['ngRoute', 'mancalaModule']);


mancalaApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/game/:id', {
        templateUrl: 'templates/game.html',
        controller: 'gameController'
    }).
     otherwise({
        templateUrl: 'templates/lobby.html',
        controller: 'lobbyController'
     });

}]);