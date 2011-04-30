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

exports.badRequest = function(test) {
  test.expect(1);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    java.sendRequest('BadClassName',
        {},
        function(err, response) {
          test.ok(err);
          
          java.shutdown();
          test.done();
        }
    );
  });
};

exports.echo = function(test) {
  test.expect(3);

  var java = new Java();
  java.onInitialised(function initialiseEvent() {
    java.sendRequest('uk.co.pekim.nodejava.nodehandler.echo.EchoHandler',
        {text: 'something', number: 1},
        function(err, response) {
          test.ok(!err);
          
          test.strictEqual(response.text, 'something');
          test.strictEqual(response.incrementedNumber, 2);

          java.shutdown();
          test.done();
        }
    );
  });
};

//exports.multiple = function(test) {
//  test.expect(20);
//
//  var java = new Java();
//  java.onInitialised(function initialiseEvent() {
//    for (var c = 0; c < 20; c++) {
//      send(test, java);
//    }
//    
//    moreRequests(test, java);
//  });
//};
//
//function send(test, java) {
//  var start = Date.now();
//  
//  java.sendRequest('uk.co.pekim.nodejava.nodehandler.echo.EchoHandler',
//      {text: 'something', number: 1},
//      function(response) {
//        var end = Date.now();
//
//        console.log((end - start) + 'ms')
//      });
//}
//
//function moreRequests(test, java) {
//  setTimeout(function() {
//    console.log('+++++++++++++++++++++++++++++++');
//    for (var c = 0; c < 4; c++) {
//      send(test, java);
//    }
//    
//    moreRequests(test, java);
//  }, 500);
//}

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
