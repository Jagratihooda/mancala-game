var mancalaModule = angular.module('mancalaModule', []);


mancalaModule.controller('gameController', ['$rootScope', '$routeParams', '$scope', '$http',
    function (rootScope, routeParams, scope, http) {
        rootScope.stompClient = null;

        rootScope.reload = function getData() {

            http.get('/board/current-board').success(function (data) {
                scope.data = data
                scope.gameBoard = [];
                data.pits.forEach(function (pit) {
                    scope.gameBoard[pit.position] = pit.stoneCount;
                })
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/board/player-in-action').success(function (data) {
                scope.gameTurn = data;
            }).error(function (data, status, headers, config) {
                scope.errorMessage = "Failed do load game properties";
            });
            http.get('/board/current-state').success(function (data) {
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
            http.post('/board/make-move?position='+id).success(function (data) {
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
                location.path('/board/' + rootScope.gameId);
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


mancalaModule.controller('gamesToBeJoinedByController', ['$rootScope', '$scope', '$http', '$location',
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
                    location.path('/board/' + data.id);
                })
            }
        };

        rootScope.reloadGamesToJoin();
    }]);


mancalaModule.controller('playerOwnGamesController', ['$rootScope', '$scope', '$http', '$location', '$routeParams',
    function (rootScope, scope, http, location, routeParams) {
        rootScope.reloadPlayerGames  =function () {

            scope.playerGames = [];

            http.get('/lobby/player/own-games/list').success(function (data) {
                scope.playerGames = data;
            });

            scope.loadGame = function (id) {
                rootScope.gameId = id;
                http.get('/board/' + id).success(function (data) {
                    location.path('/board/' + id);
                })
            }
        };

        rootScope.reloadPlayerGames();

    }]);

var mancalaApp = angular.module('mancalaApp', ['ngRoute', 'mancalaModule']);


mancalaApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    when('/board/:id', {
        templateUrl: 'templates/board.html',
        controller: 'gameController'
    }).
     otherwise({
        templateUrl: 'templates/lobby.html',
        controller: 'lobbyController'
     });

}]);