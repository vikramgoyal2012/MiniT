<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: vikramgoyal
  Date: 7/22/13
  Time: 7:24 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="/static/css/bootstrap.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/bootstrap-responsive.css" type="text/css">
</head>
<body>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="brand" href="#">Mini Twitter</a>

                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li class="active"><a id="Home" href="#">Home</a></li>
                        <li><a href="Profile" id="Profile">Profile</a></li>
                        <li><a href="#" id="Logout">Logout</a></li>
                    </ul>
                    <form class="navbar-form pull-right">
                     <input class="span2" type="text" placeholder="Search Query">
                     <button type="submit" class="btn">Search</button>
                    </form>
                                 </div><!--/.nav-collapse -->
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                    <div class="hero-unit" id="tweetbox">

                    <textarea class="span14" rows="8" id="tweetContent"></textarea>
                    <p>

                         <div id="charactersRemaining" style="display: inline">140</div>
                          &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp
                        <button class="btn btn-primary" type="submit">Tweet</button>

                    </p>
                    </div>
                         </div><!--/span-->
            <div class="span9">
                <div class="hero-unit">
                    Welcome,Vikram
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>
                                0 <a id="showMyTweets" href="#">Tweets</a>
                            </th>
                            <th>
                                1 <a id="showWhoseFollowingMe" href="#">Follower</a>
                            </th>
                            <th>
                                1 <a id="showWhoIAmFollowing" href="#">Following</a>
                            </th>

                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="row-fluid">
                   <div class="span12">
                       <table class="table"  id="contentTable">
                           <thead>
                           <tr>
                               <th id="contentTitle">
                                   Tweets
                               </th>
                           </tr>
                           </thead>
                           <tbody id="contentBody">
                           <tr>
                               <td>
                                   Tweet No1
                               </td>
                           </tr>
                           <tr>
                               <td>
                                   Tweet No2

                               </td>
                           </tr>


                           </tbody>
                       </table>

                   </div>
                </div><!--/row-->
                  </div><!--/span-->
        </div><!--/row-->
        <div class="row-fluid">
            <div class="span3">
                <div class="hero-unit">
                    Trends - Coming Soon
                </div>
            </div>
        </div>
    </div>
 <%--   <div class="container-fluid">

        <div class="row-fluid">
            <div class="span3">
                <div class="well sidebar-nav">
                        <textarea class="span10" rows="6"></textarea>
                     <p>
                      <label id="characters remaining">   140 characters remaining
                      </label>
                    <button class="btn btn-primary" type="submit">Tweet</button>
                    </p>
                </div>
                </div>
            <div class="span9">
                <div class="hero-unit">
                     Welcome,Vikram
                     <table class="table table-bordered">
                         <thead>
                         <tr>
                             <th>
                                 0 Tweets
                             </th>
                             <th>
                                 1 Follower
                             </th>
                             <th>
                                 1 Following
                             </th>

                         </tr>
                         </thead>
                     </table>

                 </div>

            </div>

        </div>
      </div>
      <%--
    <div class="row">
    <div class="span12" id="basicuserinfo">
      <div class="container">
        <div class="hero-unit">
            Welcome,Vikram
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>
                        0 Tweets
                    </th>
                    <th>
                        1 Follower
                    </th>
                    <th>
                        1 Following
                    </th>

                </tr>
                </thead>
            </table>

        </div>
      </div>
            </div>
        </div>
     <%--   <div class="span8" id="usercontentheader">
              <div class="hero-unit">
              This space will be filled by something else
              </div>

<div class="row">
    <div class="span4" id="trends">
      <div class="hero-unit">
          Trends
      </div>
        </div>
    <div class="span8" id="usercontentbody">
        <table class="table">
            <thead>
            <tr>
                <th>
                    Tweets
                </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    Tweet No1
                </td>
            </tr>
            <tr>
                <td>
                    Tweet No2

                </td>
             </tr>


            </tbody>
        </table>
    </div>
</div>
--%>
            <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
            <script src="/static/js/bootstrap.min.js"></script>
           <script src="/static/js/bootstrap-transition.js"></script>
            <script src="/static/js/bootstrap-alert.js"></script>
            <script src="/static/js/bootstrap-modal.js"></script>
            <script src="/static/js/bootstrap-dropdown.js"></script>
            <script src="/static/js/bootstrap-scrollspy.js"></script>
            <script src="/static/js/bootstrap-tab.js"></script>
            <script src="/static/js/bootstrap-tooltip.js"></script>
            <script src="/static/js/bootstrap-popover.js"></script>
            <script src="/static/js/bootstrap-button.js"></script>
            <script src="/static/js/bootstrap-collapse.js"></script>
            <script src="/static/js/bootstrap-carousel.js"></script>
            <script src="/static/js/bootstrap-typeahead.js"></script>
    <script>
        $(document).ready(function(){
            $('#showWhoIAmFollowing').click(function(){

                //alert("You will be shown who you are following");

                $('#contentTitle').html("Following");
                var content = '<tr><td>following him</td></tr> <tr><td>Following him also</td></tr>'
                $('#contentTable tbody').html(content);

            })
        })

    </script>
    <script>
        $(document).ready(function(){
            $('#showMyTweets').click(function(){

                //alert("You will be shown tweets composed by you");

                $('#contentTitle').html("Tweets");
                var content = '<tr><td>My Tweet1</td></tr> <tr><td>My Tweet2 </td></tr>'
                $('#contentTable tbody').html(content);

            })
        })

    </script>

    <script>
        $(document).ready(function(){
            $('#showWhoseFollowingMe').click(function(){

                //alert("You will be shown who is following you");

                $('#contentTitle').html("Followers");
                var content = '<tr><td>Follower 1</td></tr> <tr><td>Follower 2</td></tr>'
                $('#contentTable tbody').html(content);

            })
        })

    </script>

 <script>
     $(document).ready(function(){
       $('#tweetContent').keyup(function(){
           var charactersEntered = $('#tweetContent').val();
           var characterLength = charactersEntered.length;

                if(characterLength>140) {
                    $('#tweetContent').val(charactersEntered.substr(0,140));
                    $('#charactersRemaining').html("0");
                }
                else {
                    var charactersRemaining = 140 - characterLength;
                    $('#charactersRemaining').html(charactersRemaining);
                }

         }   )
     })
 </script>

        </body>

</html>