#!/bin/sh -xu
for K in GrowingFinals PinyinSyllables StandardQwerty; do
	convert -background white -alpha remove -layers OptimizePlus +dither -delay 60 -loop 0 "$K"_*.png "$K".gif
done
