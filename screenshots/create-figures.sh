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





for i in 1 2 3 4 5 6; do
    convert "GrowingFinals_Nihao_0000${i}.png" \
        -gravity Center -background black -extent 470x400 \
        -gravity NorthWest -fill white -pointsize 55  -annotate +30+30 "${i})" \
        "$OUTPUT/sequence_GrowingFinals_$i.png"
done

convert $OUTPUT/sequence_GrowingFinals_* -background none +append "$OUTPUT/sequence_GrowingFinals.png"



for i in 1 2 3 4 5 6; do
    convert "PinyinSyllables_Shanghai_0000${i}.png" \
        -gravity Center -background black -extent 470x400 \
        -gravity NorthWest -fill white -pointsize 55  -annotate +30+30 "${i})" \
        "$OUTPUT/sequence_PinyinSyllables_$i.png"
done

convert $OUTPUT/sequence_PinyinSyllables_* -background none +append "$OUTPUT/sequence_PinyinSyllables.png"
