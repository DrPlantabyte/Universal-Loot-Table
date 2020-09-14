#!/bin/bash
cd "$(dirname "$0")"
TARGET_DIR="output/app-image"
rm -rf "$TARGET_DIR"
jlink --output "$TARGET_DIR" --module-path "$JAVA_HOME/jmods:build" --add-modules drplantabyte.ult.fxui --launcher ULT=drplantabyte.ult.fxui/drplantabyte.ult.fxui.App --no-header-files --no-man-pages
