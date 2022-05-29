/**
 * @author Joyce Hong
 * @email soja0524@gmail.com
 * @create date 2019-09-02 20:51:10
 * @modify date 2019-09-02 20:51:10
 * @desc socket.io server !
 */

const express = require('express');
const bodyParser = require('body-parser');


const socketio = require('socket.io')
var app = express();

// parse application/x-www-form-urlencoded
// { extended: true } : support nested object
// Returns middleware that ONLY parses url-encoded bodies and 
// This object will contain key-value pairs, where the value can be a 
// string or array(when extended is false), or any type (when extended is true)
app.use(bodyParser.urlencoded({ extended: true }));

//This return middleware that only parses json and only looks at requests where the Content-type
//header matched the type option. 
//When you use req.body -> this is using body-parser cause it is going to parse 
// the request body to the form we want
app.use(bodyParser.json());


var server = app.listen(3000, () => {
    console.log('Chat Server is running on port number 3000')
})


//Chat Server

var io = socketio.listen(server)
var rooms = new Array();
rooms.push("Everything about Love", "Apple fans", "Samsung lovers","Is Google seeing this?")

io.on('connection', function (socket) {

    console.log(`Connection : SocketId = ${socket.id}`)
    var userName = '';

    socket.on('roomList', function (data) {
        console.log('roomList trigged')
        socket.join(`roomList`)
        io.to(`roomList`).emit('roomList', rooms);
    })

    socket.on('subscribe', function (data) {
        console.log('subscribe trigged')
        const room_data = JSON.parse(data)
        userName = room_data.userName;
        const roomId = room_data.roomId;

        socket.join(`${roomId}`)
        console.log(`Username : ${userName} joined Room Name : ${roomId} / Rooms now: ${rooms}`)
        io.to(`${roomId}`).emit('newUserToChatRoom', `${userName}=>${roomId}`);

        if (rooms.indexOf(roomId) == -1) {
            rooms.push(roomId)
            io.to(`roomList`).emit('roomList', rooms);
        }
    })

    socket.on('unsubscribe', function (data) {
        console.log('unsubscribe trigged')
        const room_data = JSON.parse(data)
        const userName = room_data.userName;
        const roomId = room_data.roomId;

        console.log(`Username : ${userName} leaved Room Name : ${roomId}`)
        socket.broadcast.to(`${roomId}`).emit('userLeftChatRoom', userName)
        socket.leave(`${roomId}`)
    })

    socket.on('newMessage', function (data) {
        console.log('newMessage triggered')

        const messageData = JSON.parse(data)
        const messageContent = messageData.messageContent
        const roomId = messageData.roomId
        const messageTime = messageData.dateMillis

        console.log(`[Room Number ${roomId}] ${userName} : ${messageContent}`)
        const chatData = {
            userName: userName,
            messageContent: messageContent,
            roomId: roomId,
            dateMillis: messageTime
        }
        console.log(`Object to broadcast > ${JSON.stringify(chatData)}`)
        io.to(`${roomId}`).emit('updateChat', JSON.stringify(chatData))
    })

    socket.on('disconnect', function () {
        console.log("One of sockets disconnected from our server.")
    });
})

module.exports = server; //Exporting for test