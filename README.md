# cordova-plugin-mixpanel
A cordova plugin for mixpanel.

- [android sdk version 4.6.4](https://github.com/mixpanel/mixpanel-android/tree/v4.6.4)
- [ios sdk version 2.9.9](https://github.com/mixpanel/mixpanel-iphone/releases/tag/v2.9.9)

#### Install

```
cordova plugin add https://github.com/suspiration/cordova-plugin-mixpanel.git
```

#### ios
After successfully installing the plugin, need to manually add Mixpanel.framework as an "Embedded Binaries" within Xcode.  Within Xcode, go to General tab for the build target, then add Mixpanel.framework in the Embedded Binaries section.