#!/usr/bin/env bash

# Description : Build Project for docker
# Author      : chench9@lenovo.com
# Date        : 2018-08-15

profile=docker
config_src=src/main/resources/application.properties
config_tmp=src/main/resources/application.properties.tmp
pom_src=pom.xml
pom_tmp=pom.xml.tmp
pom_docker=docker.xml
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
    mv -f $pom_src $pom_tmp
    mv -f $pom_docker $pom_src
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
    mv -f $pom_src $pom_docker
    mv -f $pom_tmp $pom_src
    rm -rf $static_dir
}

echo "Build start..."
setup
packageJar
clean
echo "Done."


