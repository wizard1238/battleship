var webSocket = require('ws')
var { v4: uuidv4 } = require('uuid');

var wss

exports.createWSS = function(server) {
    wss = new webSocket.Server({server: server})

    var clients = {}
    var matches = {}

    wss.on('connection', function(ws) {
        console.log('connected pog')

        var clientId = uuidv4()

        clients[clientId] = {}
        clients[clientId].ws = ws

        ws.on('message', function(msg) {
            try {
                msg = JSON.parse(msg) //For this to work, you have to send {"key": "value"}
            } catch (error) {
                ws.send('Incorrect JSON Format')
                return
            }
            

            if (msg == 'dio') {
                ws.send('you were expecting a response, but it was me, dio!')
                console.log("wow dio")
            } else if (msg.option == 'new') {
                ws.send(createNewGame(clientId))
            } 
            else {
                ws.send('You sent: ' + msg)
            }

            
        })

        ws.on('close', function() {
            delete clients[clientId]
            console.log(clients)
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
}