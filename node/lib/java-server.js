var spawn = require('child_process').spawn,
    util = require('util'),
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

Server.prototype.kill = function () {
  this.javaProcess.kill();
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
