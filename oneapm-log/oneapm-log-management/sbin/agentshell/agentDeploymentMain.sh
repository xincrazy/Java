#!/bin/sh

bin=`dirname "${BASH_SOURCE-$0}"`
HOME=`cd "$bin"; pwd`
dir=$HOME

if [ -z $(which expect) ]; then
    yum install expect
fi

ip=$1
user=$2
passwd=$3
#tar源目录
tarSrc=$4
#tar文件名
tarName=$5
#解压目录
tarDir=$6

sh $dir/deployAgent.sh $ip $user $passwd $tarSrc $tarName $tarDir|grep \#
