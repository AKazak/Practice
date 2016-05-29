<%--
  Created by IntelliJ IDEA.
  User: BelMAPO
  Date: 24.05.2016
  Time: 11:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-COMPATIBLE" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="Style.css">
    <link rel="stylesheet" href="sweetalert.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <title>Login</title>
</head>
<body>
    <div class="row">
        <div class="col-sm-8 col-sm-offset-2">
            <form method="post" action="/login" class="form-horizontal">
                <div class="form-group">
                    <label class="label-control col-sm-1 col-sm-offset-4"> Name </label>
                    <div class="col-sm-4">
                        <input type="text" name="login" class="form-control" placeholder="login">
                    </div>
                </div>
                <div class="form-group">
                    <label class="label-control col-sm-1 col-sm-offset-4"> Password </label>
                    <div class="col-sm-4">
                        <input type="password" name="password" class="form-control" placeholder="password">
                    </div>
                </div>
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-primary col-sm-2 col-sm-offset-5">Log in</button>
                </div>
            </form>
        </div>
    </div>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
</body>
</html>
