#!/bin/bash

total=0
passed=0

javac -cp ./lib/po-uilib.jar -d ./out `find src -name *.java`

for x in tests/*.in; do
    if [ -e ${x%.in}.import ]; then
        java -cp ./lib/po-uilib.jar:./out -Dimport=${x%.in}.import -Din=${x} -Dout=${x%.in}.outhyp mmt.app.App;
    else
        java -cp ./lib/po-uilib.jar:./out -Din=${x} -Dout=${x%.in}.outhyp mmt.app.App;
    fi

    diff -cB -w ${x%.in}.out ${x%.in}.outhyp > ${x%.in}.diff ;
    ((total += 1))
    if [ -s ${x%.in}.diff ]; then
        echo "FAIL: ${x%.in}. See file .diff";
    else
        echo ${x};
        rm -f ${x%.in}.diff ${x%.in}.outhyp ;
    ((passed += 1))
    fi
done

echo "Done."
echo "Passed" ${passed} "out of" ${total}

rm saved01 saved03