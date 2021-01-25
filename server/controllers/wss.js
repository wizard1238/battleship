var webSocket = require('ws')
var { v4: uuidv4 } = require('uuid');

var wss

exports.createWSS = function(server) {
    wss = new webSocket.Server({server: server})

    var clients = {}
    var matches = {}

    wss.on('connection', function(ws) {
        var clientId = uuidv4()

        clients[clientId] = {}
        clients[clientId].ws = ws

        ws.on('message', function(msg) {
            if (msg == 'dio') {
                ws.send('you were expecting a response, but it was me, dio!')
            } else if (msg == 'new') {
                var matchId = uuidv4()
                
                clients[clientId].matchId = matchId
                ws.send(matchId)
                console.log(matchId)
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
}