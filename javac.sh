#!/bin/bash
cd "$(dirname "$0")"
rm -rvf build/*
ERRORS=0
for MODULE in $(ls modules); do
	echo -e "\n\nCompiling $MODULE..."
	javac -d build --module-source-path modules --module "$MODULE"
	if test $? -ne 0; then ERRORS=1; fi;
done
if test $ERRORS -ne 0; then echo "Build aborted due to error"; exit 1; fi;
echo -e "\n\n...build successful"
