UniqueDigits = function () {

  cursor = db.phones.find();
  loop: while ( cursor.hasNext() ) {
    var phone = cursor.next();
    var num = phone._id.toString().slice(2,-1);
    for (var i = 0; i < 9; i++) {
      for (var j = 0; j < i; j++){
        if (num[i]==num[j]) {
          continue loop;
        }
      }
    }
    print(num)
  }
}
