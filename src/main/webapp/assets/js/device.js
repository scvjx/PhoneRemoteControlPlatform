wsHost = getURL();
wsPort = '9010';
wsWarName = 'PhoneRemoteControlPlatform';
canvas = document.getElementById('phone-screen');
screensize = 0;
screenw = 0;
screenh = 0;
isDown = false;
canvas.width = 440;
canvas.height = 860;
clickxflag = 0;
clickyflag = 0;
recordscript = false;
scriptnumber = 1;
testscript = "";
testscriptcn = "";
touchstarttime = "";
//function setConnected(connected) {
//        document.getElementById('disconnect').disabled = !connected;
//};

function getURL(){
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPaht = curWwwPath.substring(0, pos);
    var flag1 = localhostPaht.indexOf("//");
    var flag2 = localhostPaht.lastIndexOf(":");
    var rootPath = "";
    if(flag2!=-1){
        rootPath = localhostPaht.substring(flag1+2,flag2);
    }else{
        rootPath = localhostPaht.substring(flag1+2);
    }
    return rootPath;

}

function connect(deviceinfo) {
    console.log(deviceinfo);
    sn = deviceinfo.split("-")[0];
    screensize = deviceinfo.split("-")[1];
    screenw = screensize.split("x")[0];
    screenh = screensize.split("x")[1];
    if ('WebSocket' in window) {
        console.log('Websocket supported');
        socket = new WebSocket('ws://'+wsHost+':'+wsPort+'/'+wsWarName+'/websocket?sn='+sn);
        console.log('Connection attempted');

        socket.onopen = function () {
            console.log('Connection open!');
        }

        socket.onclose = function () {
            console.log('Disconnecting connection');
            if (socket != null) {
                socket.close();
                socket = null;
            }
        }
        socket.onmessage = function (evt) {
            if(typeof(evt.data)=="string"){
                console.log("发送后台点击数据"+evt.data);
                socket.send(evt.data);
            }else{
                var data = evt.data;
                imageDeal(data);
            }
        }
    }
};

function imageDeal(data) {
    var URL = window.URL || window.webkitURL;
    var img = new Image();
    img.onload = function () {
        var ctx = canvas.getContext('2d');
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
        img.onLoad = null;
        img = null;
        u = null;
    };
    var u = URL.createObjectURL(data);
    img.src = u;
    canvas.img = img;
};

function disconnect() {
    setConnected(false);
    console.log("Disconnected");
    if (socket != null) {
        socket.close();
        socket = null;
    }
};

canvas.onmousedown = function (event) {
    isDown = true;
    var pos = getXAndY(canvas, event);
    sendDown(pos.x, pos.y, false);
};

canvas.onmouseup = function (event) {
    if (!isDown) {
        return;
    }
    isDown = false;
    var pos = getXAndY(canvas, event);
    sendUp(pos.x, pos.y, false);
};
canvas.onmousemove = function (event) {
    if (!isDown) {
        return;
    }
    var pos = getXAndY(canvas, event);
    sendMove(pos.x, pos.y, false);
};

canvas.onmouseout = function (event) {
    if (!isDown) {
        return;
    }
    isDown = false;
    var pos = getXAndY(canvas, event);
    sendUp(pos.x, pos.y, false);
};

// 获取鼠标在控件的相对位置
function getXAndY(control, event){
    //鼠标点击的绝对位置
    var Ev= event || window.event;
    var mousePos = mouseCoords(event);
    var x = mousePos.x;
    var y = mousePos.y;
    //alert("鼠标点击的绝对位置坐标："+x+","+y);

    //获取div在body中的绝对位置
    var x1 = control.offsetLeft+10;
    var y1 = control.offsetTop+90;

    //鼠标点击位置相对于div的坐标
    var x2 = x - x1;
    var y2 = y - y1;
    return {x:x2,y:y2};
};
// 获取鼠标在html中的绝对位置
function mouseCoords(event){
    if(event.pageX || event.pageY){
        return {x:event.pageX, y:event.pageY};
    }
    return{
        x:event.clientX + document.body.scrollLeft - document.body.clientLeft,
        y:event.clientY + document.body.scrollTop - document.body.clientTop
    };
};

function sendDown(argx, argy, isRo) {
    var scalex = screenw / canvas.width;
    var scaley =  screenh  / canvas.height;
    var x = argx, y = argy;
    if (isRo) {
        x = (canvas.height - argy) * (canvas.width / canvas.height);
        y = argx * (canvas.height / canvas.width);
    }
    x = Math.round(x * scalex);
    y = Math.round(y * scaley);
   clickxflag = x;
   clickyflag = y;
   touchstarttime = (new Date()).getTime();

};


function sendUp(argx, argy,isRo) {
    var scalex = screenw / canvas.width;
       var scaley =  screenh  / canvas.height;
       var x = argx, y = argy;
       if (isRo) {
           x = (canvas.height - argy) * (canvas.width / canvas.height);
           y = argx * (canvas.height / canvas.width);
       }
       x = Math.round(x * scalex);
       y = Math.round(y * scaley);
       touchendtime = (new Date()).getTime();
       timestamp = touchendtime - touchstarttime;
       touchstarttime = "";
       if(clickxflag == x && clickyflag == y && timestamp<=1000){
          command = "t "+x+" "+y;
          recordScript("t",x,y);
          console.log(command);
       }if(timestamp>1000){
             command = "p "+clickxflag+" "+clickyflag+" "+x+" "+y;
          recordScript("s",clickxflag,clickyflag,x,y);
          }
       else{

          command = "s "+clickxflag+" "+clickyflag+" "+x+" "+y;
          recordScript("s",clickxflag,clickyflag,x,y);
       }
       sendTouchEvent(command);
}

function sendTouchEvent(Str) {
    socket.send(Str);
};

function sendMove(argx, argy, isRo) {
    var scalex = screenw/ canvas.width;
    var scaley = screenh / canvas.height;
    var x = argx, y = argy;
    if (isRo) {
        x = (canvas.height - argy) * (canvas.width / canvas.height);
        y = argx * (canvas.height / canvas.width);
    }
    x = Math.round(x * scalex);
    y = Math.round(y * scaley);


}

function setRecordScript(recflag){
    this.recordscript = recflag;
}

function recordScript(actiontype,x1,y1,x2,y2){
    if(recordscript){

        if ( $("#script").length == 0 ) {
            $("#recordscriptdiv").append("<table id='script' name='script'>");
        }

        if(actiontype=="t"){
        console.log("touch"+x1+y1);
         var tbBody="<tr id='step"+scriptnumber+"'><td>点击坐标("+x1+","+y1+")</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td></tr>"
         $("#script").append(tbBody);
         scriptnumber++;
         testscript +="t "+x1+" "+y1+"\n";
         testscriptcn +="点击坐标("+x1+","+y1+")|";
        }
        if(actiontype=="s"){
        console.log("swipe"+x1+y1+x2+y2);
        var tbBody="<tr id='step"+scriptnumber+"'><td>滑动("+x1+","+y1+")到：（"+x2+","+y2+")</td><td><button id='del"+scriptnumber+"' onclick='delscript("+scriptnumber+");'>删除</button></td></tr>"
        $("#script").append(tbBody);
         scriptnumber++;
         testscript +="s "+x1+" "+y1+" "+x2+" "+y2+"\n";
         testscriptcn +="滑动("+x1+","+y1+")到：（"+x2+","+y2+")|";
        }
    }
}



