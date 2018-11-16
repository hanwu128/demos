//1、加载http核心模块
var http = require('http');

//2、使用http.createServer()方法创建一个web服务器，返回一个server实例
var server = http.createServer();

//3、发送请求，接收响应
server.on('request', function (request, response) {
    console.log('收到请求' + request.url);
    response.writeHeader(200, {'Content-Type': 'text/html;charset:utf-8'});
    response.write('<head><meta charset="utf-8"/></head>');

    if (request.url === '/test') {
        response.write('你好');
    } else if (request.url === '/user') {
        response.write('你好:张三');
    }

    response.end();
});

//4、绑定端口号，启动服务器
server.listen(3000, function () {
    console.log('服务器启动成功')
});