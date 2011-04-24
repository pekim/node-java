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
  var options = {
      host: 'localhost',
      port: server.port(),
      path: '/',
      method: 'POST'
    };

  var request = http.request(options, function(response) {
    var data = "";

    response.on('data', function(chunk) {
      data += chunk;
    });

    response.on('end', function() {
      callback(JSON.parse(data));
    });
  });

  // write data to request body
  request.write(JSON.stringify(request));
  request.end();
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
