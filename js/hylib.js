define(["jquery"], function($) {
  var mybind = function(oThis) {
    if (typeof this !== "function") {
      throw new TypeError("Trying to bind a non-callable object " +
                          (typeof this));
    }
    var aArgs = Array.prototype.slice.call(arguments, 1);
    var fToBind = this;
    var fNOP = function() {};
    var fBound = function() {
      return fToBind.apply(this instanceof fNOP ? this : oThis,
                           aArgs.concat(Array.prototype.slice.call(arguments)));
    }
    fNOP.prototype = fToBind.prototype;
    fBound.prototype = new fNOP();
    return fBound;
  };

  function foo() {
    this.b = 100;
    console.log("this is in foo, b = " + this.b);
    return this.a;
  }

  function bar(x, y) {
    console.log("this is in bar");
    console.log(this + " " + x + " " + y);
  }

  foo.mybind = mybind;
  bar.mybind = mybind;

  var func1 = foo.mybind({a:2});
  var func2 = bar.mybind({a:3}, 50);
  
  console.log("Create o = new foo():");
  var o = new foo();
  o.b = 200;
  // this is a bug, o is not successfully bound.
  console.log("Bind o to foo as func3 and call func3():");
  var func3 = foo.mybind(o);
  func3();

  console.log("Direct call of func1 and func2:");
  var x = new func1();
  var y = new func2();

  return {
    mybind: mybind,
    func1 : func1,
    func2 : func2,
  };

});
