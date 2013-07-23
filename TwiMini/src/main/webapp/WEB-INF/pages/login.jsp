<%--
  Created by IntelliJ IDEA.
  User: vikramgoyal
  Date: 7/22/13
  Time: 1:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bootstrap nested columns example</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="/static/css/bootstrap.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css" type="text/css"/>
    <style type="text/css">
     .container { background-color: #fff; border: 10px;}
        .registration-form {display: none}
    </style>

</head>
<body>


<div class="container">
    <div class="content">
        <div class="row">
            <div class="span4 offset8">
            <div class="login-form" id="login-form">
                <h2>Login</h2>
                <form action="/login" method="post">
                    <fieldset>
                            <div class="clearfix">
                            <input type="text" placeholder="Username">
                        </div>
                        <div class="clearfix">
                            <input type="password" placeholder="Password">
                        </div>
                        <label class="checkbox">
                            <input type="checkbox">Remember Me
                        </label>
                            <button class="btn btn-primary" type="submit">Sign in</button>
                            <a id="newuser"> New User?</a>
                    </fieldset>
                </form>
            </div>

                <div class="registration-form" id="registration-form">
                    <h2>Register</h2>
                    <form action="">
                        <fieldset>
                            <div class="clearfix">
                                <input type="text" placeholder="Email">
                            </div>
                            <div class="clearfix">
                                <input type="password" placeholder="Name">
                            </div>
                            <div class="clearfix">
                                <input type="password" placeholder="Password">
                            </div>

                            <button class="btn btn-primary" type="submit">Register</button>
                            <a id="signin">Sign-in</a>
                                                    </fieldset>
                    </form>
                </div>

            </div>
        </div>

<%--
<%
     <div class="container">
    <div class="row">
        <div class="span7">
            <div class="row">
                <div class="span4">
                    <p>Maecenas aliquet velit vel turpis. Mauris neque metus, malesuada nec, ultricies sit amet, porttitor mattis, enim. In massa libero, interdum nec, interdum vel, blandit sed, nulla. In ullamcorper, est eget tempor cursus, neque mi consectetuer mi, a ultricies massa est sed nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Proin nulla arcu, nonummy luctus, dictum eget, fermentum et, lorem. Nunc porta convallis pede.</p>
                </div>
                <div class="span3">
                    <p>Maecenas aliquet velit vel turpis. Mauris neque metus, malesuada nec, ultricies sit amet, porttitor mattis, enim. In massa libero, interdum nec, interdum vel, blandit sed, nulla. In ullamcorper, est eget tempor cursus, neque mi consectetuer mi, a ultricies massa est sed nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Proin nulla arcu, nonummy luctus, dictum eget, fermentum et, lorem. Nunc porta convallis pede.</p>
                </div>
            </div>
        </div>
        <div class="span5">
            <p>Maecenas aliquet velit vel turpis. Mauris neque metus, malesuada nec, ultricies sit amet, porttitor mattis, enim. In massa libero, interdum nec, interdum vel, blandit sed, nulla. In ullamcorper, est eget tempor cursus, neque mi consectetuer mi, a ultricies massa est sed nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Proin nulla arcu, nonummy luctus, dictum eget, fermentum et, lorem. Nunc porta convallis pede.</p>
        </div>
    </div>
</div>
    %>
    --%>
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="/static/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function(){
                $('#newuser').click(function(){
                  $('.login-form').hide();
                    //alert("Login Hidden");
                    $('.registration-form').show();

                });
            })
        </script>
        <script>
            $(document).ready(function(){
                $('#signin').click(function(){
                    //alert("Login Hidden");
                    $('.registration-form').hide();
                    $('.login-form').show();

                });
            })
        </script>


</body>
</html>











