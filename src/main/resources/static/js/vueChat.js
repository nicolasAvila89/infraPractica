new Vue({
    el: '#app',
    data: {
        title: 'Por favor conectese al chat',
        nickname: "",
        message:"",
        connection: null,
        messages:[]
    },
    methods: {
        connect: function () {
            this.connection= new ChatConnection();
            var _this=this;
            this.connection.connect(function(){
                _this.title="Esta conectado al chat.. comienze a hablar"
            },function(msj){
                _this.addMessage(msj);
            });
        },
        disconnect: function () {
            var _this=this;
            this.connection.disconnect(function(){
                _this.title="Se ha desconectado del chat, vuelva a conectarse";
                _this.connection=null;
                _this.messages=[];
                _this.nickname="";
            });
        },
        send: function () {
            this.connection.sendMessage(this.nickname,this.message);
            this.message="";
        },
        addMessage:function(msj){
            this.messages.push(msj);
        }
    }
})