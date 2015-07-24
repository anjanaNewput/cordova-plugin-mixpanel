var Mixpanel = function() {
};

//set mixpanel project token
Mixpanel.prototype.init = function(token, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'init', [token]);
};

//set mixpanel project token
Mixpanel.prototype.alias = function(token, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'alias', [token]);
};

//identify a user
Mixpanel.prototype.identify = function(identity, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'identify', [identity]);
};

//track an event
Mixpanel.prototype.track = function(name, properties, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'track', [name, properties]);
};

//register super properties for all events
Mixpanel.prototype.registerSuperProperties = function(properties, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'registerSuperProperties', [properties]);
};

//add push device token to people
Mixpanel.prototype.peopleAddPushDeviceToken = function(token, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'peopleAddPushDeviceToken', [token]);
};

//identify people
Mixpanel.prototype.peopleIdentify = function(identity, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'peopleIdentify', [identity]);
};

//set user's profile
Mixpanel.prototype.peopleSet = function(properties, successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'peopleSet', [properties]);
};

//upload data to mixpanel
Mixpanel.prototype.flush = function(successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'flush', []);
};

//reset mixpanel
Mixpanel.prototype.reset = function(successCallback, failureCallback) {
  return cordova.exec(successCallback, failureCallback, 'Mixpanel', 'reset', []);
};

module.exports = new Mixpanel();
