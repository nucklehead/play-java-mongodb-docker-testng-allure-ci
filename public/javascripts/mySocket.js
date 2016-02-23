var sendTask;
$(function() {
    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var taskSocket = new WS("ws://localhost:9000/taskSocket")

    var form = document.getElementById('myForm');
    var taskName = document.getElementById('taskName');
    var taskDescription = document.getElementById('taskDescription');
    var taskList = document.getElementById('taskList');

    sendTask = function() {
        taskSocket.send(JSON.stringify(
            {
                name: taskName.value,
                description: taskDescription.value
            })
        );
    }
    var receiveEvent = function(event) {
        var data = JSON.parse(event.data)

        // Handle errors
        if(data.error) {
            chatSocket.close()
            console.log(data.error);
            return
        } else {
        }

        var el = $('<li class="list-group-item">'+
                         '<span class="badge"></span>'+
                         '<h4 class="list-group-item-heading">'+
                             '<span class="glyphicon glyphicon-tags"></span>'+
                         '</h4>'+
                         '<p class="list-group-item-text"></p>'+
                     '</li>')
        // Create the message element
        $("span", el).text(data.status)
        $("h4", el).text(data.name)
        $("p", el).text(data.description)
        taskList.append(el)

    }


    taskSocket.onmessage = receiveEvent

})