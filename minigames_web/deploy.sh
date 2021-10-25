#!/bin/bash
set -ex
docker build -t derkades/minigames-web .
docker push derkades/minigames-web
