var Server = require('../lib/node-server'),
    http = require('http');

exports.port = function(test) {
  var server = new Server();

  test.ok(server.port() > 0);

  test.done();
};

exports.message = function(test) {
  var server = new Server();

  test.expect(1);
  
  server.on('initialised', function onMessage(message) {
    test.strictEqual(message.type, 'initialised');
    
    test.done();
  });

  process.nextTick(function() {
    var options = {
        host: 'localhost',
        port: server.port(),
        path: '/',
        method: 'POST'
      };

    var request = http.request(options, function(res) {
    });

    // write data to request body
    request.write(JSON.stringify({type: 'initialised'}));
    request.end();
  });
};
