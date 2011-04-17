var Java = require('../lib/java');

exports.initialised = function(test) {
  test.expect(1);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    test.ok(true);
    test.done();
  });
};

exports.shutdown = function(test) {
  test.expect(1);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    java.sendRequest({type: 'shutdown'}, function(response) {
      test.ok(true);
      test.done();
    });
  });
};
