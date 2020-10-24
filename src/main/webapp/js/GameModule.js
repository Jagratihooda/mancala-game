var gameModule = angular.module('gameModule', []);

gameModule.controller('lobbyController', ['$rootScope', '$scope', '$http', '$location',

    function (rootScope, scope, http, location) {

        rootScope.stompClient = null;

        scope.createNewGame = function () {

            http.post("/lobby/game/prepare", {
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                }
            }).success(function (data, status, headers, config) {

                rootScope.gameId = data.id;
                location.path('/lobby/' + rootScope.gameId);
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });
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


gameModule.controller('gamesToJoinController', ['$rootScope', '$scope', '$http', '$location',
    function (rootScope, scope, http, location) {
        rootScope.reloadGamesToJoin = function () {
            scope.gamesToJoin = [];
            http.get('/lobby/to-be-joined/game/list').success(function (data) {
                scope.gamesToJoin = data;
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });


            scope.joinGame = function (id) {
                var requestUrl = "/lobby/join/game?id=" + id;
                http.post(requestUrl, {
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).success(function (data) {
                    rootScope.gameId = data.id;
                    location.path('/lobby/' + data.id);
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

            http.get('/lobby/player/own-games/list').success(function (data) {
                scope.playerGames = data;
            }).error(function (data, status, headers, config) {
                location.path('/player/panel');
            });

            scope.loadGame = function (id) {
                console.log(id);
                rootScope.gameId = id;
                http.get('/lobby/' + id).success(function (data) {
                    location.path('/lobby/' + id);
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
                console.log('Connected: ' + frame);
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
var mancalaApp = angular.module('mancalaApp', ['ngRoute', 'gameModule']);




mancalaApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/player', {
        templateUrl: 'templates/lobby.html',
        controller: 'lobbyController'
    }).
    when('/lobby/:id', {
        templateUrl: 'templates/game.html',
        controller: 'gameController'
    }).

    otherwise({
        redirectTo: '/player'
    });
}]);