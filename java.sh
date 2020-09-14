#!/bin/bash
cd "$(dirname "$0")"
java --module-path build --module drplantabyte.ult.fxui/drplantabyte.ult.fxui.App -d run
