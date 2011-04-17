var util = require('util'),
    events = require('events'),
    NodeServer = require('./node-server'),
    JavaServer = require('./java-server'),
    Java;

Java = function () {
  var self = this;
  
  self.nodeServer = new NodeServer(),
  self.javaServer = new JavaServer();

  events.EventEmitter.call(self);
  
  self.javaServer.start({nodePort: self.nodeServer.port()});

  self.nodeServer.on('initialised', function onMessage(message) {
    self.javaServer.port(message.port);
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
  this.javaServer.sendRequest(request, callback);
};

module.exports = Java;
