#!/bin/sh
USER=$(expr $(cat pilot-study-participants.txt | wc -l) + 1)
echo "USER: $USER"

read -e -p "NAME: " NAME
echo "$USER $NAME" >> pilot-study-participants.txt

java -cp "bin:lib/*" ntu.csie.prompter.Prompter \
  -Participant $USER \
  -PhraseCount 5 \
  -StudyPlan pilot-study-plan.tsv \
  -PhraseSet pilot-study-phrase-set.txt \
  -Record pilot-study-record.tsv
