FROM nginx:1.22.0-alpine
ENV TZ=Africa/Tunis
RUN apk add --no-cache tzdata
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# Changing access right for running container without root privilege 
RUN chmod -R 0777 /var /run
EXPOSE 82
ADD ./dist /usr/share/nginx/html/
