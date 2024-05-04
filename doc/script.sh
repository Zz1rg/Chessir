#! /bin/sh
CLASS_LOCATION="/home/luna/University/Java Project/MainProgmethProject/out/production/SimpleChess"

IFS=$'\n'
for f in $(fd -t file -a .class "$CLASS_LOCATION")
do
    unset IFS
    FILE_NAME=$(echo "$f" | sed -r "s/.+\/(.+)\..+/\1/")
    echo "$f"
    javap -p "$f" > ${FILE_NAME}.txt
    IFS=$'\n'
done
unset IFS
