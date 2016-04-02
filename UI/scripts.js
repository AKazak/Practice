var author = "Guest";
var messageList = [];
var textBox = document.getElementById('textBox');

function run(){
    var buttonSend = document.getElementById('messageSend');
    buttonSend.addEventListener('click', send);

    var buttonLogin = document.getElementById('logger');
    buttonLogin.addEventListener('click', login);

    var appContainer = document.getElementsByClassName('textBox')[0];
    appContainer.addEventListener('click', delegateEvent);

    loadFromStorage();
    render();
}

function getText(el) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(el);
    pre.appendChild(text);
    return pre.innerHTML;
}

function newMessage(text, author, edit, time, timestamp, del, me) {
    return {
            message: text,
            auth: author,
            isEdit: !!edit,
            timestamp: time,
            editTimestamp: timestamp,
            id: uniqueId(),
            isDeleted: del,
            isMine: !!me
    };
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random() * Math.random();
    return Math.floor(date * random);
}

function render(){
    for(var i = 0; i < messageList.length; i++){
        renderMessage(messageList[i]);
    }
    renderAuthor(author);
}

function renderMessage(mess) {
    var list = document.getElementsByClassName('textBox')[0];
    if (mess.isMine) {
        var element = myElementFromTemplate();
        renderMyMessageAttributes(element, mess);
    }
    else {
        var element = elementFromTemplate();
        renderMessageAttributes(element, mess);
    }
    list.appendChild(element);
    element.scrollIntoView();
}

function renderAuthor(author){
    document.getElementById("me").innerHTML = author;
}

function myElementFromTemplate() {
    var template = document.getElementById("my-message-template");
    return template.firstElementChild.cloneNode(true);
}

function elementFromTemplate() {
    var template = document.getElementById("others-message-template");
    return template.firstElementChild.cloneNode(true);
}

function renderMessageAttributes(element, mess) {
    element.setAttribute('data-message-id', mess.id);
    element.lastElementChild.textContent = mess.message;
    if (mess.isEdit) {
        element.firstElementChild.textContent = mess.auth + ", " + mess.timestamp;
        element.firstElementChild.nextElementSibling.textContent = "Edited " + mess.editTimestamp;
    }
    else element.firstElementChild.textContent = mess.auth + ", " + mess.timestamp;
    if (mess.isDeleted) {
        element.lastElementChild.style.opacity = "0.5";
        element.lastElementChild.textContent = "This message has been deleted.";
    }
}

function renderMyMessageAttributes(element, mess) {
    var iconEdit = '<a href="#"><i class="fa fa-pencil fa-fw edit"></i></a>';
    var iconDelete = '<a href="#"><i class="fa fa-trash-o fa-lg delete"></i></a>'
    element.setAttribute('data-message-id', mess.id);
    element.lastElementChild.textContent = mess.message;
    if (mess.isEdit) {
        element.firstElementChild.innerHTML = mess.auth + ", " + mess.timestamp + iconEdit + iconDelete;
        element.firstElementChild.nextElementSibling.textContent = "Edited " + mess.editTimestamp;
    }
    else {
        element.firstElementChild.innerHTML = mess.auth + ", " + mess.timestamp + iconEdit + iconDelete;
    }
    if (mess.isDeleted) {
        element.firstElementChild.firstElementChild.nextElementSibling.remove();
        element.firstElementChild.firstElementChild.remove();
        element.lastElementChild.style.opacity = "0.5";
        element.lastElementChild.textContent = "This message has been deleted.";
    }
}

function saveMessages(listToSave) {
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    localStorage.setItem("Message history", JSON.stringify(listToSave));
}

function loadMessages() {
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("Message history");
    return item && JSON.parse(item);
}

function loadFromStorage(){
    author = loadAuthor() || "Guest";
    messageList = loadMessages() || [newMessage("Welcome to our chart!", "System", false, (new Date()).toLocaleString(), false, false)];
}

function saveAuthor(){
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    localStorage.setItem("Author", author);
}

function loadAuthor(){
    if (typeof(Storage) == "undefined") {
        swal('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("Author");
    return item;
}

function send(e) {
    e.preventDefault();
    var messageInput = document.getElementById('message');
    if(messageInput.value === "") {
        return false;
    } else {
        var text = getText(messageInput.value);
        var date = new Date();
        messageInput.value = "";
        var message = newMessage(text, author, false, date.toLocaleString(), 0, false, true);
        renderMessage(message);
        messageList.push(message);
        saveMessages(messageList);
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
    saveAuthor(author);
}

function editName() {
    swal({
        title: "Edit your name!",
        text: "Write something interesting:",
        type: "input",
        showCancelButton: true,
        closeOnConfirm: false,
        animation: "pop",
        inputValue: author,
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
        saveAuthor(author);
        swal("Nice!", "Your new name: " + inputValue, "success");
    });
}

function delegateEvent(evtObj) {
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
            iconDelete.remove();
            iconEdit.remove();
            var messageId = messageBlock.getAttribute("data-message-id");
            for (var i = 0; i < messageList.length; i++) {
                if (messageList[i].id == messageId) {
                    messageList[i].isDeleted = true;
                }
            }
            message.innerHTML = "This message has been deleted.";
            message.classList.add("deleted");
            saveMessages(messageList);
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
        inputValue: messageText,
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
        var messageId = messageBlock.getAttribute("data-message-id");
        for (var i = 0; i < messageList.length; i++) {
            if (messageList[i].id == messageId) {
                messageList[i].isEdit = true;
                messageList[i].message = getText(inputValue);
                messageList[i].editTimestamp = new Date().toLocaleString();
            }
        }
        message.innerHTML = getText(inputValue);
        var myInfo = messageBlock.firstElementChild;
        var info = myInfo.nextElementSibling;
        var editInfo = "Edited  " + new Date().toLocaleString();
        info.innerHTML = editInfo;
        saveMessages(messageList);
        swal.close();
    });
}