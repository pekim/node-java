var Server = require('../lib/java-server'),
    http = require('http');

exports.start = function(test) {
  var nodeServer,
      server;

  nodeServer = http.createServer(function (request, response) {
    request.addListener('data', function(data) {
      var message = JSON.parse(data);
      test.strictEqual(message.type, 'initialised');
      
      server.kill();
      test.done();
    });
  });

  nodeServer.listen();
  
  server = new Server();
  server.start({nodePort: nodeServer.address().port});
};
