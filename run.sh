#!/bin/bash

for file in $(ls -1 Day*.java | sort); do
    echo ----- $file -----
    javac $file 
    java $(echo $file | sed -e 's/.java//')
done

rm *.class

