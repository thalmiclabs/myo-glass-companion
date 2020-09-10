# Myo Glass Companion Protocol Input

<a href="https://market.myo.com/app/55243f41e4b0526c9cb198c0/">
  <img alt="Android app on Myo Market"
       src="https://s3.amazonaws.com/thalmicdownloads/misc/myo_market_badge.png" />
</a>

> North (formerly Thalmic Labs), the creator of the Myo armband, was acquired by Google in June 2020. Myo sales ended in October 2018 and Myo software, hardware and SDKs are no longer available or supported. [Learn more.](https://support.getmyo.com)

Uses Glass Companion Bluetooth protocol to connect to Google Glass Explorer Edition and control the native ui/interface using the &copy;Myo Gesture Control Armband (thalmic.com/myo).

This is a basic proof of concept and should not be considered the best practice for a gesture based HUD interface. Gesture are mapped directly the comparable gestures on the Glass trackpad. For example, swipe right to swipe the card to the right:

|Myo Pose       | Glass Touchpad Command|
|---------------|-----------------------|
|Double Tap     | Unlock Myo            |
|Wave Right     | Swipe Right           |
|Wave Left      | Swipe Left            |
|Fist           | Tap/Select            |
|Fingers Spread | Swipe Down/Go Back    |

##How to use:
1. Ensure MyGlass app and background processes are not running (Kill from Android Application Manager).
1. Compile, install and run.
1. Myo and Glass will be found automatically if you have paired with them before. In this case, skip steps 4 and 5.
1. Press 'Choose Myo' to connect to your Myo device.
1. Press 'Choose Glass' to connect to your Glass device. Glass must be paired to continue.
1. Perform the 'Unlock' pose (Double-Tap) to enable Myo control of Glass for two seconds, turning the app's icon from gray (off/neutral) to blue (active). When Myo is unlocked, additional gestures will continue to allow Myo to control Glass for 2 seconds.
1. Gestures should be shown on phone UI, and commands send to the Glass device. Glass must be awake/active to respond to touch commands.

##Limitations:
- Currently no way to wake Glass up from Myo gestures.
- Glass features that rely on a phone connection (such as GPS) will not work since the MyGlass application cannot be running at the same time.

## Thanks

Using GlassBluetoothLibrary found here:
[https://github.com/thorikawa/GlassBluetoothProtocol]

Also thanks to examples/work from [@thorikawa](https://github.com/thorikawa) from GlassRemote project:
[https://github.com/thorikawa/GlassRemote]

## License

The Myo Glass Companion project is licensed using the modified BSD license. For more details, please see LICENSE.txt.

