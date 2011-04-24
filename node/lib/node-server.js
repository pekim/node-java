var http = require('http'),
    util = require('util'),
    events = require('events'),
    Server;

Server = function () {
  var self = this;
  
  events.EventEmitter.call(self);

  self.server = http.createServer(function (request, response) {
    var data = "";

    request.on('data', function(chunk) {
      data += chunk;
    });

    request.on('end', function() {
      var message = JSON.parse(data);
      
      self.emit(message.type, message);
      
      response.writeHead(200, {'Content-Type': 'application/json'});
      response.end(JSON.stringify({}));
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
