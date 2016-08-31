#!/bin/sh
tarDir=$1
tarName=$2
bin=`dirname "${BASH_SOURCE-$0}"`
if [ ! -d $tarDir ];then
    mkdir -p $tarDir
fi

chmod 777 $bin/$tarName
tar -zvxf $bin/$tarName -C $tarDir
echo '#'$?
