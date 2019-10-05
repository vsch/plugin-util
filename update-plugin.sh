#!/usr/bin/env bash
HOME_DIR="/Users/vlad/src/projects/plugin-util"

cd ${HOME_DIR} || exit

cp plugin-util.jar "/Volumes/Pegasus/Data"
cp lib/flexmark-util.jar "/Volumes/Pegasus/Data"
echo updated plugin-util.jar in "/Volumes/Pegasus/Data"

cp plugin-util.jar ../MissingInActions/lib
cp lib/flexmark-util.jar ../MissingInActions/lib
echo updated plugin-util.jar in ../MissingInActions/lib

cp plugin-util.jar ../touch-typists-completion-caddy/lib
cp lib/flexmark-util.jar ../touch-typists-completion-caddy/lib
echo updated plugin-util.jar in ../touch-typists-completion-caddy/lib

cp plugin-util.jar ../CLionArduinoPlugin/lib
cp lib/flexmark-util.jar ../CLionArduinoPlugin/lib
echo updated plugin-util.jar in ../CLionArduinoPlugin/lib

#cp plugin-util.jar ../idea-multimarkdown3/lib
#echo updated plugin-util.jar in ../idea-multimarkdown3/lib

#cp plugin-util.jar ../idea-multimarkdown2/lib
#cp lib/flexmark-util.jar ../idea-multimarkdown2/lib
#echo updated plugin-util.jar in ../idea-multimarkdown2/lib
#
#cp plugin-util.jar ../idea-multimarkdown1/lib
#cp lib/flexmark-util.jar ../idea-multimarkdown1/lib
#echo updated plugin-util.jar in ../idea-multimarkdown1/lib
