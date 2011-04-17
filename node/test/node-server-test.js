var Server = require('../lib/node-server'),
    net = require('net'),
    netstring = require('netstring');

exports.port = function(test) {
  var server = new Server();

  test.ok(server.port() > 0);

  test.done();
};

exports.message = function(test) {
  var server = new Server(),
      socket;

  test.expect(1);
  
  server.on('initialised', function onMessage(message) {
    test.strictEqual(message.type, 'initialised');
    
    test.done();
  });

  process.nextTick(function() {
    socket = net.createConnection(server.port());
    socket.on('connect', function connect() {
      socket.write(netstring.nsWrite(JSON.stringify({type: 'initialised'})));
    });
  });
};
