#!/bin/bash

for file in $(ls -1 *.java | sort); do
    echo ----- $file -----
    javac $file 
    java $(echo $file | sed -e 's/.java//')
done

rm *.class

