# Pinyin Aware: Adaptive Chinese Input Methods for Smartwatches

## Abstract

Smartwatches, wearable electronics, and other miniature devices are becoming increasingly popular. However, text input on such small devices remains a challenge due to small form factors, especially for non-Latin languages that require more complex text entry techniques. We implement and compare 3 fully functional soft keyboards for typing Mandarin Chinese using the Hanyu Pinyin system on the latest generation of circular smartwatch devices. We introduce 2 such novel adaptive keyboards, Growing Finals and Pinyin Syllables, which change dynamically based on the current input and the limited number of possible subsequent letters, optimize available screen size, and improve the user experience on small screens.
Our evaluation is based on a user study with 15 participants and shows input speeds of around 19.4 CCPM for Growing Finals and 18.5 CCPM for Pinyin Syllables after 20 minutes of practice. More than half of the participants ultimately preferred one of these input methods over the standard QWERTY keyboard due to reduced error rates and more efficient input mechanics.


## Three Pinyin Input Methods

We have designed and implemented 3 Chinese keyboards for circular smartwatches. Each prototype has been implemented natively for Android Wear 2.0 and tested on an LG Watch Style. This smartwatch has a round 1.2" (30 mm) screen.


### Growing Finals
![GrowingFinals](https://raw.githubusercontent.com/rednoah/dual-swipe-pinyin/master/screenshots/GrowingFinals.gif "GrowingFinals")

The Growing Finals keyboard exploits an intrinsic characteristic of the Pinyin romanization system: out the 26 letters of the Latin alphabet, 23 can appear as the first letter in a Pinyin syllable, while the remaining 0 to 4 Latin letters of any Pinyin syllable are composed from only 9 unique Latin characters. Liu, et al. have previously exploited this characteristic in PinyinPie.
Further research revealed that after entering the first letter from a list of 23 options, entering the remaining letters of any Pinyin syllable requires no more than 6 unique possible next letters (including the syllable separator ' if necessary) in any input state.
Hence, the Growing Finals keyboard will present a full QWERTY keyboard in its initial state and - depending on the input - adapt and enlarge possible next letters while preserving the general QWERTY layout. This allows for significantly larger keys after entering the initial letter for each syllable and thus improved input speed and accuracy according to Fitts' law.


### Pinyin Syllables
![PinyinSyllables](https://raw.githubusercontent.com/rednoah/dual-swipe-pinyin/master/screenshots/PinyinSyllables.gif "PinyinSyllables")

Each Pinyin syllable can be seen as a combination of an initial sound and a final sound. Initials with similar sounds (e.g. b and p) tend to be combined with a similar set of final sounds (e.g. bang and pang) due to language characteristics. An analysis of the language model reveals that for each of the 26 initial sounds, there are between 2 and 24 (M = 15.4, SD = 5.6) possible final sounds (including no final sound for vowels such as a and e).
The Pinyin Syllables input method implements a 2-stage adaptive keyboard layout based on Pinyin initials and finals which allows users to enter any Pinyin syllable with exactly 2 keystrokes. The first stage uses a pseudo-QWERTY keyboard layout to leverage existing familiarity with the standard QWERTY keyboard layout. The u, i, and v keys have been removed, and keys for sh, zh, and ch have been added so that each key on the initial keyboard corresponds to exactly one initial sound in the Pinyin language model. Once an initial key has been entered, the keyboard will adapt and display all possible finals for the current input state. Helping with visual search, finals with similar sounds are grouped by colour and placed near corresponding QWERTY keys if possible. Depending on the entered initial, the second stage may contain between 2 and 24 buttons allowing for significantly larger buttons in most cases with the added benefit that a single key press that completes the pinyin syllable may correspond to more than one Latin letter.


### Standard Qwerty
![StandardQwerty](https://raw.githubusercontent.com/rednoah/dual-swipe-pinyin/master/screenshots/StandardQwerty.gif "StandardQwerty")

We implement the standard QWERTY keyboard with support for Chinese character entry in addition to our two novel input methods to serve as a baseline for comparison. This keyboard design is used by almost all native speakers from China and language students from abroad that type Chinese characters on a computer or smartphone and thus very familiar to most user.

