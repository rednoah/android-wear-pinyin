#!/bin/sh -xu

OUTPUT="$PWD/figures"


banner_layouts=(
    GrowingFinals PinyinSyllables StandardQwerty
)
letters=(
    a b c d e f g
)


for i in "${!banner_layouts[@]}"; do
    convert "${banner_layouts[$i]}.png" \
        -gravity Center -background black -extent 470x400 \
        -gravity NorthWest -fill white -pointsize 55  -annotate +30+30 "${letters[$i]})" \
        "$OUTPUT/banner_${banner_layouts[$i]}_${letters[$i]}.png"
done

convert $OUTPUT/banner_* -background none +append "$OUTPUT/stripe_banner_layouts.png"
