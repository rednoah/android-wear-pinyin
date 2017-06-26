#!/bin/sh
adb shell screencap -p > "`date | tr ':' '.'`.png"
