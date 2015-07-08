#!/bin/bash

FAILED=0
JAVACC=$HOME/bin/javacc-6.0

function recompile()
{
    printf "Recompiling %s ...\n" $1
    if [[ $1  == *"small_c.jjt"* ]]; then
        if [[ "$2" == false ]]; then
            cp -r src/compiler temp/
        fi
        cd temp/compiler
        java -classpath $JAVACC/bin/lib/javacc.jar jjtree small_c.jjt
        java -classpath $JAVACC/bin/lib/javacc.jar javacc small_c.jj
        cd ../../
        javac -classpath temp temp/compiler/*.java -d build
        if [[ $? -ne 0 ]]; then
            FAILED=1
        fi
    elif [[ $1 == *"small_c_preprocessor.jjt"* ]]; then
        if [[ "$2" == false ]]; then
            cp src/$1 temp/$1
        fi
        cd temp/preprocessor
        java -classpath $JAVACC/bin/lib/javacc.jar javacc small_c_preprocessor.jj
        cd ../../
        javac -classpath temp temp/preprocessor/SmallC_PreProcessor.java -d build
        if [[ $? -ne 0 ]]; then
            FAILED=1
        fi
    elif [[ $1 == *".java"* ]]; then
        if [[ "$2" == false ]]; then
            cp src/$1 temp/$1
        fi
        javac -cp temp temp/$1 -d build
        if [[ $? -ne 0 ]]; then
            FAILED=1
        fi
    else
        if [[ "$2" == false ]]; then
            cp $1 build/$1
        fi
    fi
}

if [[ -f hashes.sha ]]; then
    if [[ -n $(shasum -c hashes.sha | awk '{if ($2 != "OK"){print $1}}') ]]; then
        for i in $(shasum -c hashes.sha | awk '{if ($2 != "OK"){print $1}}');
        do
            i=$(echo $i | sed 's/:$//g')
            i=$(echo $i | sed 's/src\///g')

            recompile $i false

        done
    fi

    if [[ $FAILED -ne 1 ]]; then
        printf "" > hashes.sha
        for i in $(find src snippets -type f ! -name '*.swp');
        do
            shasum $i >> hashes.sha
        done
    fi
else
    make all
fi
