#!/bin/sh

bin=`dirname "${BASH_SOURCE-$0}"`
HOME=`cd "$bin"; pwd`
dir=$HOME

ip=$1
user=$2
passwd=$3
#tar源目录
tarSrc=$4
#tar文件名
tarName=$5
#解压目录
tarDir=$6

#tarDir=/tmp/
#ip=127.0.0.1
#user=songzhichao
#passwd=qaz123456
#tarName=apache-flume-1.6.0-bin.tar.gz
#tarSrc=source/${tarName}

script=installation.sh

expect <<-EOF
set time 30
spawn scp ${dir}/${script} ${user}@${ip}:/tmp/${script}
expect {
"*password:" { send "${passwd}\r";exp_continue}
"no)?" {send "yes\r"; exp_continue}
}
EOF

expect <<-EOF
set time 30
spawn scp ${tarSrc} ${user}@${ip}:/tmp/${tarName}
expect {
"*password:" { send "${passwd}\r";exp_continue}
"no)?" {send "yes\r"; exp_continue}
}
EOF

expect <<-EOF
set time 30
spawn ssh ${user}@${ip} "sh /tmp/${script} ${tarDir} ${tarName}"
expect {
"*password:" { send "${passwd}\r";exp_continue}
"no)?" {send "yes\r"; exp_continue}
}
EOF

expect <<-EOF
set time 30
spawn ssh ${user}@${ip} "rm -rf /tmp/${script}"
expect {
"*password:" { send "${passwd}\r";exp_continue}
"no)?" {send "yes\r"; exp_continue}
}
EOF
expect <<-EOF
set time 30
spawn ssh ${user}@${ip} "rm -rf /tmp/${tarName}"
expect {
"*password:" { send "${passwd}\r";exp_continue}
"no)?" {send "yes\r"; exp_continue}
}
EOF
