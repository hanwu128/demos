#!/bin/bash

current_path=`pwd`
case "`uname`" in
	Linux)
		bin_absolute_path=$(readlink -f $(dirname $0)) 
		;;
	*)
		bin_absolute_path=`cd $(dirname $0);pwd`
		;;
esac

export LANG=en_US.UTF-8
base_dir=${bin_absolute_path}/..
conf_dir=$base_dir/conf
log_dir=$base_dir/logs
pid_file=$base_dir/bin/pid

## set java path
if [ -z "$JAVA" ]; then
	JAVA=$(which java)
fi

if [ -z "$JAVA" ]; then
	echo "java not install, Please install java first" 2>&2
	exit 1
fi

## set classpath
CLASSPATH=.:$base_dir/conf
for i in $base_dir/libs/*;
do
	CLASSPATH="$CLASSPATH":$i
done

#str=`file -L $JAVA | grep 64-bit`
#if [ -n "$str" ]; then
#	OPTS_JAVA_MEM="-server -Xms512m -Xmx1024m"
#else
#	OPTS_JAVA_MEM="-server -Xms512m -Xmx1024m"
#fi

OPTS_APP="-Dlogback.configurationFile=$conf_dir/logback.xml -Dlog.dir=$log_dir"
OPTS_JAVA_EXT="-Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
OPTS_JAVA="$OPTS_JAVA_EXT $OPTS_APP"

nohup $JAVA $OPTS_JAVA -classpath $CLASSPATH com.lenovo.iot.digitaltwin.DigitaltwinApplication >/dev/null 2>&1 & echo $! > $pid_file

echo "Started."


