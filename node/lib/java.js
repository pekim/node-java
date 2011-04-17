var util = require('util'),
    events = require('events'),
    NodeServer = require('./node-server'),
    JavaServer = require('./java-server'),
    Java;

Java = function () {
  var self = this,
      nodeServer = new NodeServer(),
      javaServer = new JavaServer();

  events.EventEmitter.call(self);
  
  javaServer.start({nodePort: nodeServer.port()});

  nodeServer.on('initialised', function onMessage(message) {
    javaServer.port = message.port;
    self.initialised = true;

    self.emit('initialised');
  });
};

util.inherits(Java, events.EventEmitter);

Java.prototype.onInitialised = function(callback) {
    if (this.initialised) {
      callback();
    } else {
      this.on('initialised', callback);
    }
};

Java.prototype.sendRequest = function(request, callback) {
};

module.exports = Java;
