#!/bin/sh

CONF_FILE="/app/production.conf"

if [ ! -f "$CONF_FILE" ] || [ ! -s "$CONF_FILE" ]; then
  tee -a "$CONF_FILE" <<EOF
include "application"
play.http.secret.key="$(echo $(head -c 64 /dev/urandom | base64))"
# awk removes the trailing spaces
play.http.forwarded.trustedProxies=["::1", "127.0.0.1", "$(hostname -I | awk '$1=$1 print1')/16"]
EOF
fi

# RUNNING_PID at the root of the app is not automatically removed when the container is restarted
# https://www.playframework.com/documentation/2.8.x/ProductionConfiguration#Changing-the-path-of-RUNNING_PID
rm -f /var/run/gryffinium.pid

/app/bin/gryffinium -Dconfig.file=$CONF_FILE -Dpidfile.path=/var/run/gryffinium.pid -J--add-exports -Jjava.base/sun.net.www.protocol.file=ALL-UNNAMED
