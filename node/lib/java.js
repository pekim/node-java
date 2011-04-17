var util = require('util'),
    events = require('events'),
    nodeServer = require('./node-server'),
    javaServer = require('./java-server'),
    Java;

Java = function () {
  var self = this;

  events.EventEmitter.call(self);
  
  javaServer.start({port: nodeServer.port()});

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
