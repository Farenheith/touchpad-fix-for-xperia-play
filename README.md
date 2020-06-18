First of all, I need to say something about the touchpad implementation:
there's many ways to implement the touchpad on the Xperia Play. Some of these ways works with CM9, and somes works only with the Sony's framework. This is a little obviously, as some games recognize the touchpad on CM9, and others don't.

I know about the fix we can make on the build.prop. But this will only work with the games that do a check on the device model and implements the touchpad in a compatible way with CM9.

# FAQ

## Why (programmatically) some games that uses touchpad does not work on CM9/CM10

I can't say about all games, but some games uses the event onTouchEvent of the classes View or Activity to get the touchpad return. The CM9 does not support this return on this event. What Sony did on their framework is a way to activate the touchpad return over this event, that's not present on AOSP

## What this fix does?
This fix change the CM9/CM10 framework.jar to, transparently, activate the touchpad return on the onTouchEvent. So, the games that uses this method to recognize the touchpad will begin to work.

## How I use this fix?
### FOR ICS USERS
#### FIRST: EDIT YOUR BUILD.PROP
First of all, you need to edit the build.prop because many games use some properties there to recognize the phone as a Xperia play! There's no a real rule, so, some games can look to one property, and others to another. Here you can take a look in what to do. Also, edit the build.prop can solve the circle button problem. here you can read why.

#### SECOND: FLASH THE ZIP
Do a nandroid backup and, then, flash the appropriated zip for your android version.

#### THIRD: INSTALL THE TOUCHPADACTIVATOR (FOR VERSION 0.6+)
Some games just can't activate the touchpads because some verifications that it did, like compare if the SDK Version é lesser than 13. Change this property and some others can cause many problems on the phone, so, I created an app to switch when the game need to activate the touchpad and when it's always activated. SOME APPS HAVE PROBLEMS TO DEAL WITH THE TOUCHPAD WHEN IT'S ALWAYS ACTIVATED, LIKE FPSE, so, with this app, you can switch the touchpad behavior as you need.

IMPORTANT:DO THE BACKUP OF YOUR FRAMEWORK.JAR, DO NOT DELETE IT, JUST IN CASE. If you want, you can do a nandroid backup to be more safe.


FOR JB USERS For now, you need to edit the ROM flashable zip. Extract the framework.jar from the JB fix and replace directly on the system/framework folder of the ROM zip. Then, extract the build.prop from the ROM zip, do the described changes, and put it back. After that, flashthe edited ROM zip and install the touchpad activator. The flashable zip is not working, idk why yet. For now, this is the only supported way until I can solve this.

The 'ALWAYS ACTIVATED' behavior that we can choose in the TouchpadActivator app can drain the battery life?
No. The battery life is not influenced by the chosen behavior. The term "ALWAYS ACTIVATED" just means that the touchpad events is always forwarded to the onTouchEvent, but the device is always listen to the touchpad doesn't matter what behavior you choose! (even without this fix)

## What ROMs supports this fix?
It can works on CM9/CM10 based ROMs, you need to try to be sure. I tested it on CM10 FXP137 and CM9 FXP138, and maybe the creator of some custom ROM did changes on the framework for himself, and then this fix can be incompatible with his ROM, and it's very likely.
ROMs based on CM7 will not work with this fix because I'm using a new feature of the ICS.

## Which games is fixed with this fix?
See post #3

## This fix solve once for all the touchpad issues of CM9?
I can't say that. Maybe there's other way to get the touchpad return that is not compatible with CM9 already. Maybe Sony will do a update and change this rules. Only time will telll

## My phone brick! What can I do now?
Restore your nandroid backup or rename back the old framework.jar. Just to be clear: I'm not responsible for any bricks or problems of your phone.

## After apply the fix, some apps gives FC. Why?
Some games and apps gives FC if their data is downloaded before the fix. It's normal. Try the app a second time, it works on almost every time. If it's keeping FC, then clear the app data and try again. Remember that some games download the data in two steps (first part download, then the app is closed and you need to open it again to proceed). If even with this the app keeping FC, post a logcat on this thread, the ROM you're using, what app is giving those troubles and any other info that you think it's useful.


## The touchpad is not recognizing at all. Why?
As I need to make many tests and reboot the phone... It's very likely that I let the gamepad slided down. But, after some pain I realize that when I didn't slide down the gamepad after the reboot, the touchpad doesn't work (That is, the gamepad was already slided down, so, I didn't do it after the reboot). So, if anyone experiencing this problem, just slide up and down the gamepad and test again. This is the only situation that something like that happened to me.

## What is PointerID?
pointerID is just an identification for your finger when it is touching the touchpad. It's a way to the phone know what is each finger. On GB, this ID start at 1 (finger 1, finger 2, etc...). On ICS, it start at 0 (finger 0, finger 1...). Some games do some shameful validation based that the first id will always be 1 and then get problems on GB. This is why this option is included. As it is a new resource and I'm considering it experimental, when you reboot the phone the pointerID will allways be setted to start at 0.

## The pointerid starting at 1 is working better than starting at 0. Can this be the default behavior?
Yes, you can set the pointerid starting at 1 as the default behavior. Just put on the build.prop this: mod.touchpad.startfrom1 = 1. You can do this by yourself if you feel that this new behavior is better than the default one. The idea is remove this switcher and let the pointerID permanently starting at 1 (as it is on GB), but I need to do some tests to make sure that all will work okay.

## There's a GB version planned for the future?
No. I'll not make a GB version for this fix, for two reasons:
1 - It's a completely different fix. The concept of this fix is based on the onGenericMotionEvent, that exists since ICS. To make a GB version we need to analyse the stock GB support for touchpad entirely, not just the framework java code, and probably will be only possible to implement directly in the sources.

2 - I don't have interest. I think we have to look forward, and we have GB ROMs good enough for gaming. Also soon, with the V 3.0 of the turbo kernel, we can make a dual boot with GB and desired ROM and then use GB just for games.

## Do I need to install the touchpad activator?
No. You don't need. Editing the build.prop and flashing the zip/replacing the framework probable will be enough to get good results. BUT some games need to be tricked to get the touchpad working. For example: Max Payne checks the SDK version to activate or not the touchpad. You can't change the SDK Version in the build.prop, otherwise you'll get big problems (like the data partition wiped) when you reboot the ROM. So, to avoid this problem, you need to set the touchpad behavior, in the touchpad activator, to ALWAYS ACTIVATED. The option to set the pointer ID s becoming obsolete, because every game should work with pointerID starting at 1. The next version of the activator will not have this property anymore and this option will be setted directly in the build.prop

IS VERY RECOMMENDED TO INCLUDE IN THE BUILD.PROP THE FOLLOWING LINE
Code:
mod.touchpad.startfrom1=1
This option doesn't affected any tested game and can improve the touchpad compability in anothers