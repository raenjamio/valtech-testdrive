 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2673ac5373bd4f13aa403e4389862abc)](https://app.codacy.com/app/raenjamio/valtech-testdrive?utm_source=github.com&utm_medium=referral&utm_content=raenjamio/valtech-testdrive&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/raenjamio/valtech-testdrive.png?branch=master)](https://travis-ci.org/raenjamio/valtech-testdrive)
[![Code Coverage](https://img.shields.io/codecov/c/github/raenjamio/valtech-testdrive/master.svg)](https://codecov.io/github/raenjamio/valtech-testdrive?branch=master)

# TEST-DRIVE 
By Rodrigo Enjamio
Encargado por Valtech

POC de un sistema de reservas de test-drive de autos, solicitado por la empresa Valtech

Tecnologias Utilizadas:
Java8: El core de la app
SpringBoot: Api para dar manejo de aplicacion Rest
H2: BD embebida para profile desa
MySQL: Para el profile prod
Log-back: Logueo
jUnit, mokito, hamcrest: Testing
Lombok: Para no declarar setters, getters etc
Maven: Gestion del proyecto
Swagger: Documentador de API endpoints.
Jacoco: Cobertura y reportes
Instrucciones para lanzar la aplicacion en localhost
Docker-compose: Generar los contenedores para prod
Map-struct: Para mappear un DTO a un Entity de forma rapida, el mas performante del mercado
Spring-security: Mediante auth basic (user="user", pass="user") le damos una seguridad basica a la aplicacion
Prometheus: Monitoreo de la aplicacion



Una vez clonado ir a la carpeta test-drive y desde la consola

 			java -jar 
Acceder via explorador a: