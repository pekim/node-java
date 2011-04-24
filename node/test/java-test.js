var Java = require('../lib/java');

exports.initialised = function(test) {
  test.expect(1);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    test.ok(true);

    java.shutdown();
    test.done();
  });
};

exports.echo = function(test) {
  test.expect(2);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    java.sendRequest('uk.co.pekim.nodejava.nodehandler.echo.EchoHandler',
        {text: 'something', number: 1},
        function(response) {
          test.strictEqual(response.text, 'something');
          test.strictEqual(response.incrementedNumber, 2);

          java.shutdown();
          test.done();
        });
  });
};

//exports.shutdown = function(test) {
//  test.expect(1);
//
//  var java = new Java();
//  java.onInitialised(function initialiseEvent() {
//    java.sendRequest({type: 'shutdown'}, function(response) {
//      test.ok(true);
//      test.done();
//    });
//  });
//};
