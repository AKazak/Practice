var author = "Guest";
var textBox = $('#textBox');

function getText(el) {
    var pre = document.createElement('pre');
    var text = document.createTextNode(el);
    pre.appendChild(text);
    return pre.innerHTML;
}

function send() {
    var message = '<div class="myBubble">' + getText($('#message').val()) + '</div>';
    var date = new Date();
    var authorInfo = '<div class="myInfo">' + date.toLocaleString() + ' ' + author + ' ' +
        '<a href="#" class="edit"><i class="fa fa-pencil fa-fw"></i></a>' +
        '<a href="#" class="delete"><i class="fa fa-trash-o fa-lg"></i></a>' + '</div>';
    $('#message').val("");
    $('#textBox').append(
        '<div class="messageBlock">' + authorInfo + message + '</div>'
    );
    textBox.scrollTop(textBox[0].scrollHeight);
}

function logName() {
    var name = getText($('#login').val());
    $('#login').val("");
    author = name;
    $('#user').html(author);
    $('#me').html(author);
    $('#login-form').hide();
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
        $('#user').html(author);
        $('#me').html(author);
        swal("Nice!", "Your new name: " + inputValue, "success");
    });
}

$(function () {
    $('#message-form').submit(function (el) {
        el.preventDefault();
        send();
    });

    $('#login-form').submit(function (el) {
        el.preventDefault();
        logName();
        $('.loggedName').show();
        $('.editNameButton').show();
    });

    $(document).on('click', '.edit', function () {
        var myMessage = $(this).parent();
        var message = myMessage.next();
        var messageText = message.html();
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
            message.html(getText(inputValue));
            var myInfo = myMessage.children();
            var editInfo = "Edited  " + new Date().toLocaleString();
            myInfo.parent().append('<div>' + editInfo + '</div>');
            swal.close();
        });
    });

    $(document).on('click', '.delete', function () {
        var message = $(this).parent().next();
        var iconEdit = $(this).prev();
        var iconDel = $(this);
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
                iconDel.remove();
                message.html("This message has been deleted.");
                message.addClass("deleted");
                swal("Deleted!", "Your message has been deleted.", "success");
            } else {
                swal("Cancelled", "Your message is safe :)", "error");
            }
        });
    });

});