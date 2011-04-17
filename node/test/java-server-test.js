var Server = require('../lib/java-server'),
    net = require('net');

exports.start = function(test) {
  var nodeServer,
      server;

  nodeServer = net.createServer(function connected(socket) {
    socket.on('data', function data(data) {
      console.log('recv : ' + data.toString('utf8'));
      test.notStrictEqual(data.toString('utf8').indexOf('initialised'), -1);
      
      server.kill();
      test.done();
    });
  });
  
  nodeServer.listen();
  
  server = new Server();
  server.start({nodePort: nodeServer.address().port});
};
