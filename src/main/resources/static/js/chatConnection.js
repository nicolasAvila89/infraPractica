class ChatConnection {


    connect(onConnect, renderMessage) {
        var socket = new SockJS('/chat');
        var _this=this;
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, function (frame) {
            onConnect();
            console.log('Connected: ' + frame);
            _this.stompClient.subscribe('/topic/messages', function (messageOutput) {
                renderMessage(JSON.parse(messageOutput.body));
            });
        });
    }

    disconnect(onDisconnect) {
        if (this.stompClient != null) {
            this.stompClient.disconnect();
        }
        onDisconnect();
        console.log("Disconnected");
    }

    sendMessage(from, text) {
        this.stompClient.send("/app/chat", {},
            JSON.stringify({'from': from, 'text': text}));
    }

}