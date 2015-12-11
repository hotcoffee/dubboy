#!/usr/bin/env bash

JAVA_CMD="java"
CPATH="./:lib/*"
ID="1"
if [ "x$2" != "x" ]
then 
	ID="$2";
fi
#服务name与applicationname一致
PIDFILE="cfg/applicationname${ID}.pid"

case $1 in
start)
	echo "Starting Server${ID}..."
	if [ -f "$PIDFILE" ]; then
      if kill -0 `cat "$PIDFILE"` > /dev/null 2>&1; then
         echo $command already running as process `cat "$PIDFILE"`. 
         exit 0
      fi
    fi
    #Main class
	${JAVA_CMD} -cp ${CPATH} -server com.ikaihuo.bis.mobile.server.Main ${ID} &
	;;
stop)
	echo "Stoping Server${ID}..."
	if [ ! -f "$PIDFILE" ]
    then
      echo "no server to stop (could not find file $PIDFILE)"
    else
		kill `cat "$PIDFILE"`
		rm "$PIDFILE"
		echo "Stoped Server${ID}!"
	fi
	exit 0
	;;
*)
    echo "Usage: $0 {start|stop} [server_id=1] [debug|release]" 
    ;;
esac
