#!/usr/bin/env bash

# Description : Build Project
# Author      : chench9@lenovo.com
# Date        : 2018-07-06

profile=$1
if [ ! -n "$profile" ]; then
    echo "Usage: sh $0 dev|test|prod|demo"
    echo "  e.g: sh build.sh dev"
    echo ""
    exit 1
fi

config_src=src/main/resources/application.properties
config_tmp=src/main/resources/application.properties.tmp
static_dir=src/main/resources/public

#
# setup
#
setup() {
    cp $config_src $config_tmp
    if [ -d $static_dir ]; then
        rm -rf $static_dir
    fi
    mkdir $static_dir
    cp -r ../digitaltwin-web/work/dist/* $static_dir
}

#
# package jar
#
packageJar() {
    echo "spring.profiles.active=$profile" >> $config_src
    mvn clean package -Dmaven.test.skip=true -P$profile
}

#
# clean
#
clean() {
    mv -f $config_tmp $config_src
    rm -rf $static_dir
}

echo "Build start..."
setup
packageJar
clean
echo "Done."

