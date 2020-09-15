#!/bin/bash
cd "$(dirname "$0")"
rm -rf output/*
jlink --output output/cli-app --module-path "$JAVA_HOME/jmods:build" --add-modules drplantabyte.ult.cli --launcher ULT=drplantabyte.ult.cli/drplantabyte.ult.cli.App --no-header-files --no-man-pages
jlink --output output/gui-app --module-path "$JAVA_HOME/jmods:build" --add-modules drplantabyte.ult.fxui --launcher ULT=drplantabyte.ult.fxui/drplantabyte.ult.fxui.App --no-header-files --no-man-pages
