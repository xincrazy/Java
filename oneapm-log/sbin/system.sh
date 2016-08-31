#!/bin/bash
 while true;
   do
   #计算内存使用率（由于free命令在不同版本的linux之间存在差异，所以采取使用差异较小的/proc/meminfo来计算内存使用率。同时由于低版本的linux中该文件的中没有MemAvailable:这一行信息，所以需要循环两个数组匹配下标的方式来计算内存使用率）
   namearray=();
   #定义一个空数组，和四个内存下标
   memTotal=0;
   memFree=1;
   memBuffer=2;
   memCached=3;
   n=0;
   for i in $(cat /proc/meminfo |sed -n '1,5p' | awk '{print $1}');
       do
           case $i in
             'MemTotal:') memTotal=$n
             ;;
             'MemFree:') memFree=$n
             ;;
             'Buffers:') memBuffer=$n
             ;;
             'Cached:') memCached=$n
             ;;
             *)
             ;;
           esac
           n=$[$n+1];
   done
   for j in $(cat /proc/meminfo |sed -n '1,5p' | awk '{print $2}');
       do
          namearray=("${namearray[@]}" "$j");
       done
   #循环利用这四个变量，对于赋予实际值，放弃下标值的使用
   memTotal=${namearray[$memTotal]};
   memFree=${namearray[$memFree]};
   memBuffer=${namearray[$memBuffer]};
   memCached=${namearray[$memCached]};
   #进行内存使用率计算
   bc1=`expr $memTotal - $memFree`;
   bc2=`expr $bc1 - $memBuffer`;
   bc3=`expr $bc2 - $memCached`;
   #计算cpu使用率,计算两个时间点之间的总的cpu时间片差值，和两个时间片的之间的空闲cpu时间片差值，计算在这一个时间段内的cpu使用率
   CPULOG_1=`cat /proc/stat | head -n1 | awk '{print $2" "$3" "$4" "$5" "$6" "$7" "$8}'`;
   SYS_IDLE_1=`echo $CPULOG_1 | awk '{print $4}'`;
   Total_1=`echo $CPULOG_1 | awk '{print $1+$2+$3+$4+$5+$6+$7}'`;



    receive=0;
    for i in $(cat /proc/net/dev | sed -n '3,$p' | grep -v 'lo' | awk '{print $2}');
        do
           let "receive=$receive+$i";
        done;

    transmit=0;
     for j in $(cat /proc/net/dev | sed -n '3,$p' | grep -v 'lo' | awk '{print $10}');
        do
           let "transmit=$transmit+$j";
        done;
     let "total=$receive+$transmit";
   dw1=`iostat -dk |awk ' {sum += $6;print sum}'|tail -1`;
   ioRead=`iostat -dk |awk ' {sum += $5;print sum}'|tail -1`;


   sleep 30;
   ioRead1=`iostat -dk |awk ' {sum += $5;print sum}'|tail -1`;
   dw2=`iostat -dk |awk ' {sum += $6;print sum}'|tail -1`;

   dw=-1;
   let "dw=$dw2-$dw1";
   ioReadSpeed=-1;
   let "ioReadSpeed=$ioRead1-$ioRead";

    receive1=0;
    for m in $(cat /proc/net/dev | sed -n '3,$p' | grep -v 'lo' | awk '{print $2}');
        do
           let "receive1=$receive1+$m";
        done;
    transmit1=0;
     for n in $(cat /proc/net/dev | sed -n '3,$p'| grep -v 'lo' | awk '{print $10}');
        do
           let "transmit1=$transmit1+$n";
        done;
     downloadSpeed=-1;
     let "downloadSpeed=$receive1-$receive";
     uploadSpeed=-1;
     let "uploadSpeed=$transmit1-$transmit";

   CPULOG_2=`cat /proc/stat | head -n1 | awk '{print $2" "$3" "$4" "$5" "$6" "$7" "$8}'`;
   SYS_IDLE_2=`echo $CPULOG_2 | awk '{print $4}'`;
   Total_2=`echo $CPULOG_2 | awk '{print $1+$2+$3+$4+$5+$6+$7}'`;
   Total=`expr $Total_2 - $Total_1`;
   SYS_IDLE=`expr $SYS_IDLE_2 - $SYS_IDLE_1`;
   #输出cpu使用率，内存使用率，磁盘最大的使用率的格式化日志
   tm=`date +%s%3N`;
     name=`hostname`;
     #计算磁盘最大使用率（计算几个文件系统，磁盘之间的最大使用率）
     df=-1;
     for i in $(df -P|sed 1d | awk '{print $5}'|tr -d %);
         do if [ $i -gt $df ];
            then df=$i;
            fi;
         done;
   echo "{\"system\":\"diskFileSystem\"","\"timestamp\":\""$tm"\",\"hostname\":\""$name"\",\"maxDiskUsage\":\""$df"\"}";
   echo "{\"system\":\"memory\"","\"timestamp\":\""$tm"\",\"hostname\":\""$name"\",\"bc3\":\""$bc3"\",\"memTotal\":\""$memTotal"\"}";
   echo "{\"system\":\"cpuUsage\"","\"timestamp\":\""$tm"\",\"hostname\":\""$name"\",\"SYS_IDLE\":\""$SYS_IDLE"\",\"Total\":\""$Total"\"}";
   echo "{\"system\":\"netIo\"","\"timestamp\":\""$tm"\",\"hostname\":\""$name"\",\"netUploadSpeed\":\""$uploadSpeed"\",\"netDownloadSpeed\":\""$downloadSpeed"\"}";
   echo "{\"system\":\"diskIo\"","\"timestamp\":\""$tm"\",\"hostname\":\""$name"\",\"ioWriteSpeed\":\""$dw"\",\"ioReadSpeed\":\""$ioReadSpeed"\"}";

  done;