#!/bin/sh
sudo mount -t nfs -o resvport,rw  192.168.40.129:/etc/asterisk /Users/leonardosuzan/TCC/Implementação/dialer/asterisk
sudo mount -t nfs -o resvport,rw  192.168.40.129:/var/lib/asterisk/sounds/custom /Users/leonardosuzan/TCC/Implementação/dialer/custom
