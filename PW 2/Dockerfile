FROM php:7.4-apache

RUN docker-php-ext-install mysqli && docker-php-ext-enable mysqli
WORKDIR /var/www/html

COPY php /var/www/html/
COPY init.sql /docker-entrypoint-initdb.d/init.sql

EXPOSE 80

