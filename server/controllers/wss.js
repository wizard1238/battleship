var webSocket = require('ws')
var { v4: uuidv4 } = require('uuid');

var wss

exports.createWSS = function(server) {
    wss = new webSocket.Server({server: server})

    var clients = {}
    var matches = {}

    wss.on('connection', function(ws) {
        console.log('connected')

        var clientId = uuidv4()

        clients[clientId] = {}
        clients[clientId].ws = ws
        clients[clientId].gameObject = {}

        ws.on('message', function(msg) {
            try {
                msg = JSON.parse(msg) //For this to work, you have to send {"key": "value"}
            } catch (error) {
                ws.send('Incorrect JSON Format')
                return
            }

            if (msg.option == 'dio') { //easter egg
                ws.send('you were expecting a response, but it was me, dio!')
                console.log("wow dio")
            } else if (msg.option == 'new') { // create new game
                clients[clientId].gameObject.matchId = createNewGame(clientId)
                ws.send(clients[clientId].gameObject)
                console.log(clients)
            } else if (msg.option == 'join') { // join a game
                clients[clientId].gameObject.matchId = joinGame(msg.matchId, clientId)
                ws.send(clients[clientId].gameObject)
            } else if (msg.option == 'ready') { // 
                if (!clients[clientId].gameObject.matchId) {
                    ws.send('No match joined')
                } else if (!matches[clients[clientId].gameObject.matchId]) {
                    ws.send('No match exists with that id')
                } else {
                    clients[clientId].gameObject.ready = true
                    ws.send(clients[clientId].gameObject)
                }
            } else if (msg.option == 'move') {
                if (!clients[clientId].gameObject.matchId) {
                    ws.send('No match joined')
                } else if (!matches[clients[clientId].gameObject.matchId]) {
                    ws.send('No match exists with that id')
                } else {
                    var otherClientId = (matches[clients[clientId].gameObject.matchId].player1 == clientId) ? player2 : player1

                    clients[otherClientId].ws.send({
                        move: msg.move
                    })
                }
            } else if (msg.option == 'response') {
                if (!clients[clientId].gameObject.matchId) {
                    ws.send('No match joined')
                } else if (!matches[clients[clientId].gameObject.matchId]) {
                    ws.send('No match exists with that id')
                } else {
                    var otherClientId = (matches[clients[clientId].gameObject.matchId].player1 == clientId) ? player2 : player1

                    clients[otherClientId].ws.send({
                        response: msg.response
                    })
                }
            }
            else {
                ws.send('You sent: ' + msg)
            }
        })

        ws.on('close', function() {
            removeGameWithClient(clientId, gameObject.matchId) //should remove match that has the client in it
            console.log('disconnected')
        })
    })

    var createNewGame = function(clientId) {
        var matchId = uuidv4()
                
        clients[clientId].matchId = matchId
        matches[matchId] = {
            player1: clientId
        }

        return matchId
    }

    var joinGame = function(matchId, clientId) {
        if (!clients[matchId]) {
            return 'No match exists with that id'
        } else {
            clients[clientId].matchId = matchId
            matches[matchId].player2 = clientId
            return matchId
        }
    }

    var removeGameWithClient = function(clientId, matchId) {
        var otherClientId = (matches[clients[clientId].gameObject.matchId].player1 == clientId) ? player2 : player1

        clients[otherClientId].ws.send('The other player has disconnected')
        delete clients[otherClientId].gameObject.matchId

        delete clients[clientId]
        delete matches[matchId]
    }
}