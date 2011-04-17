var net = require('net'),
    NetstringBuffer = require('./netstring-buffer'),
    util = require('util'),
    events = require('events'),
    Server;

Server = function () {
  var self = this;
  
  events.EventEmitter.call(self);
  
  self.server = net.createServer(function connected(socket) {
    var buffer = new NetstringBuffer();

    buffer.on('payload', function payload(payload) {
      var message = JSON.parse(payload);

      if (message.type !== 'alive') {
        self.emit(message.type, message);
      }
    });

    socket.on('data', function data(data) {
      buffer.put(data);
    });
  });
  
  self.server.listen();
  
  self.server.on('close', function() {
    self.emit('close');
  });
  
  console.log('Node server initialised on port ' + self.port());
};

util.inherits(Server, events.EventEmitter);

Server.prototype.port = function () {
  return this.server.address().port;
};

module.exports = Server;