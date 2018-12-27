'use strict';

var http = require('http');

console.log('');
console.log('Ping-pong Server');
console.log('');

var onReq = function(req, res) {
  console.log('Received %s request %s', req.method, req.url);
};

http.createServer(onReq).listen(9090);