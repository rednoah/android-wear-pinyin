#!/bin/sh
adb shell 'cd /sdcard && rm -r rime && mkdir rime && cp -r -v rime-pristine/* rime'
