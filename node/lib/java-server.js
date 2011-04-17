var spawn = require('child_process').spawn,
    util = require('util'),
    net = require('net'),
    netstring = require('netstring'),
    events = require('events'),

    jar = __dirname + '/../../target/node-java-0.0.1-SNAPSHOT-jar-with-dependencies.jar',
    Server;

Server = function () {
  var self = this;
  
  events.EventEmitter.call(self);
};

util.inherits(Server, events.EventEmitter);

Server.prototype.start = function (nodeConfiguration) {
  this.javaProcess = spawn('java', ['-jar', jar, JSON.stringify(nodeConfiguration)]);
  
  logOutput(this.javaProcess);
  logEvents(this.javaProcess);
};

Server.prototype.port = function (port) {
  this.port = port;
};

Server.prototype.kill = function () {
  this.javaProcess.kill();
};

Server.prototype.sendRequest = function(request, callback) {
  var socket = net.createConnection(this.port);

  socket.on('connect', function connect() {
    socket.write(netstring.nsWrite(JSON.stringify(request)));
  });
};

function logOutput(javaProcess) {
  javaProcess.stdout.on('data', function (data) {
    process.stdout.write(data);
  });

  javaProcess.stderr.on('data', function (data) {
    process.stderr.write(data);
  });
}

function logEvents(javaProcess) {
  javaProcess.on('exit', function (code) {
    console.log('exited : ' + code);
  });
}

module.exports = Server;
