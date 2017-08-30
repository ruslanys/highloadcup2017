FROM openjdk:8-jre-alpine

# Выбираем рабочую папку
WORKDIR /root

# Копируем наш исходный main.go внутрь контейнера, в папку go/src/dumb
ADD target/highloadcup-*.jar ./

# Открываем 80-й порт наружу
EXPOSE 80

# Запускаем наш сервер
CMD java -server \
    -XX:+PrintGC \
    -Xms3G -Xmx3700M -XX:MaxMetaspaceSize=200M \
    -jar /root/highloadcup-*.jar 80