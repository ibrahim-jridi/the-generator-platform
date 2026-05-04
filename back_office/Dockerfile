FROM nginx:1.22.0-alpine
ENV TZ=Africa/Tunis
RUN apk add --no-cache tzdata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# Changing access right for running container without root privilege 
RUN chmod -R 0777 /var /run
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 82
ADD ./dist/bs-back-office /usr/share/nginx/html/
