#!/bin/sh
adb shell 'cd /sdcard && rm -r rime rime-pristine'
adb push rime /sdcard/rime-pristine

