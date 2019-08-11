window.onload = function() {
  var buttonElem = document.getElementById('barcode-link');

  buttonElem.removeAttribute('style');

  var apiCallUrl = 'http://zxing.appspot.com/scan?ret=' + window.location.origin + '/lookup?isbn={CODE}'
  buttonElem.setAttribute('href', apiCallUrl)
};
