#!/bin/sh
adb shell 'cd /sdcard && rm -r rime'
adb shell 'cd /sdcard && mkdir rime'
adb shell 'cd /sdcard && cp -r -v rime-pristine/* rime'

