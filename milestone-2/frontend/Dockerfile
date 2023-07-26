FROM node:20
RUN mkdir /app
COPY ./src /app/src
COPY ./public /app/public
COPY ./.env /app/.env
COPY ./package.json /app/package.json
RUN cd /app && npm install
COPY ./docker-entrypoint.sh /app/docker-entrypoint.sh
ENV MQTT_HOST=localhost
ENV REST_HOST=localhost

CMD /app/docker-entrypoint.sh ${REST_HOST} ${MQTT_HOST}