FROM node:12-buster-slim as builder

WORKDIR /data
COPY . /data

COPY package.json package-lock.json ./
RUN npm install

# only now add the rest of the application code, so the npm install layer can be cached
COPY . .
RUN npm run build

FROM nginx:alpine

RUN rm -f /etc/nginx/conf.d/*
COPY nginx.conf /etc/nginx/conf.d/minigames.conf
COPY --from=builder /data/dist /var/www/html
