let stompClient = null;

function connectToWebSocketEndpoint(email) {
  const socket = new SockJS('/websocket');

  stompClient = Stomp.over(socket);
  stompClient.connect({username: email}, function(frame) {
    stompClient.subscribe('/user/queue/todo-updates', function (message) {
      console.log(message.body);
      $('#message').html(message.body);
      $('#toast').toast('show');
    });

//    if (email) {
//      stompClient.subscribe('/topic/todoUpdates/' + email, function (message) {
//        $('#message').html(message.body);
//        $('#toast').toast('show');
//      });
//    }
  });
}

function disconnectFromWebSocketEndpoint() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
}

$(document).ready(function () {
  $('#toast').toast({delay: 10000});
});
