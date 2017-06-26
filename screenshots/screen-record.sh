#!/bin/sh

# adb shell screenrecord --time-limit 30 --o raw-frames /sdcard/screenrecord.raw
# adb pull /sdcard/screenrecord.raw
# ffmpeg -f rawvideo -vcodec rawvideo -s 360x360 -pix_fmt rgb24 -r 60 -i screenrecord.raw  -an -c:v libx264 -filter:v -vf "format=fps=60,yuv420p" screenrecord.mp4


while true; do
    ./screen-grab.sh
done
