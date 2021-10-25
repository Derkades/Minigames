FROM node:12-buster-slim as builder

WORKDIR /data
COPY . /data

RUN npm --version
RUN npm install
RUN NODE_ENV=production npm run build

FROM nginx:alpine

RUN rm -f /etc/nginx/conf.d/*
COPY nginx.conf /etc/nginx/conf.d/minigames.conf
COPY --from=builder /data/dist /var/www/html
