var author = "Guest";
var textBox = document.getElementById('textBox');

function run(){
    var buttonSend = document.getElementById('messageSend');
    buttonSend.addEventListener('click', send);

    var buttonLogin = document.getElementById('logger');
    buttonLogin.addEventListener('click', login);

    var appContainer = document.getElementsByClassName('textBox')[0];
    appContainer.addEventListener('click', delegateEvent);
}

function getText(el) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(el);
    pre.appendChild(text);
    return pre.innerHTML;
}

function send(e) {
    e.preventDefault();
    var messageInput = document.getElementById('message');
    if(messageInput.value === "") {
        return false;
    } else {
        var text = getText(messageInput.value);
        var message = '<div class="myBubble">' + text + '</div>';
        var date = new Date();
        var authorInfo = '<div class="myInfo">' + date.toLocaleString() + ' ' + author + ' ' +
            '<a href="#"><i class="fa fa-pencil fa-fw edit"></i></a>' +
            '<a href="#"><i class="fa fa-trash-o fa-lg delete"></i></a>' + '</div>';
        messageInput.value = "";
        var messageBox = document.createElement('div');
        messageBox.classList.add("messageBlock");
        messageBox.innerHTML = authorInfo + message;
        textBox.appendChild(messageBox);
        messageBox.scrollIntoView();
    }
}

function login(e) {
    e.preventDefault();
    var login = document.getElementById('login');
    author = getText(login.value);
    login.value = "";
    var user = document.getElementById('user');
    user.innerHTML = author;
    var me = document.getElementById('me');
    me.innerHTML = author;
    var loginBox = document.getElementById('login-form');
    loginBox.style.display = "none";
    var logInfo = document.getElementById('logInfo');
    logInfo.style.display = "inline";
}

function editName() {
    swal({
        title: "Edit your name!",
        text: "Write something interesting:",
        type: "input",
        showCancelButton: true,
        closeOnConfirm: false,
        animation: "pop",
        inputPlaceholder: "Write something"
    }, function (inputValue) {
        if (inputValue === false) return false;
        if (inputValue === "") {
            swal.showInputError("You need to write something!");
            return false
        }
        if(inputValue.length > 20) {
            swal.showInputError("Sorry! This name is too long");
            return false
        }
        author = inputValue;
        var user = document.getElementById('user');
        user.innerHTML = author;
        var me = document.getElementById('me');
        me.innerHTML = author;
        swal("Nice!", "Your new name: " + inputValue, "success");
    });
}

function delegateEvent(evtObj) {
    console.log("asd");
    console.log(evtObj.target);
    if(evtObj.type === 'click' && evtObj.target.classList.contains('delete')){
        var messageBlock = evtObj.target.parentNode.parentNode.parentNode;
        deleteMessage(messageBlock);
    } else if(evtObj.type === 'click' && evtObj.target.classList.contains('edit')){
        var messageBlock = evtObj.target.parentNode.parentNode.parentNode;
        editMessage(messageBlock);
    }
}

function deleteMessage(messageBlock) {
    var message = messageBlock.lastElementChild;
    var iconEdit = messageBlock.firstElementChild.firstElementChild;
    var iconDelete = iconEdit.nextElementSibling;
    swal({
        title: "Are you sure?",
        text: "You will not be able to recover this message!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#E97D33",
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel plx!",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function (isConfirm) {
        if (isConfirm) {
            iconEdit.remove();
            iconDelete.remove();
            message.innerHTML = "This message has been deleted.";
            message.classList.add("deleted");
            swal("Deleted!", "Your message has been deleted.", "success");
        } else {
            swal("Cancelled", "Your message is safe :)", "error");
        }
    });
}

function editMessage(messageBlock) {
    var message = messageBlock.lastElementChild;
    var messageText = message.innerHTML;
    swal({
        title: "An input!",
        text: "Write something interesting:",
        type: "input",
        showCancelButton: true,
        closeOnConfirm: false,
        animation: "pop",
        inputPlaceholder: "Enter your message..."
    }, function (inputValue) {
        if (inputValue === false) return false;
        if (inputValue === "") {
            swal.showInputError("You need to write something!");
            return false
        }
        if (inputValue === messageText) {
            swal.close();
            return false;
        }
        message.innerHTML = getText(inputValue);
        var myInfo = messageBlock.firstElementChild;
        var info = document.createElement('div');
        var editInfo = "Edited  " + new Date().toLocaleString();
        info.innerHTML = editInfo;
        myInfo.appendChild(info);
        swal.close();
    });
}