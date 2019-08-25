# Para correr la app de prueba
./gradlew build && java -jar build/libs/gs-spring-boot-docker-0.1.0.jar

# Para buildear el docker con la version de la app
./gradlew build docker

# Para correr un container con 400Mb de memoria
docker run -e "SPRING_PROFILES_ACTIVE=prod" -e JAVA_OPTS='-Xmx400' -p <host_port>:8080 -t springio/gs-spring-boot-docker

# Enpoints de la app 
	http://localhost:8080/ (info de la memoria)
	http://localhost:8080/users/{idUser}/comprar (suma de a 100Mb de memoria)
	http://localhost:8080/users/{idUser} (para saber la cantidad de marcadores)

#Configurar NGINX
https://upcloud.com/community/tutorials/configure-load-balancing-nginx/ 
Editar load-balancer.conf y agregar dentro de html para que quede de la siguiente forma
<pre>
    upstream backend {
      server localhost:8080; 
      server localhost:8081;
      server localhost:8082;
   }
   server {
         listen 8090; 
   
         location / {
             proxy_pass http://backend;
         }
      }
</pre>


#Levantar los contenedores
<pre>
docker-compose up
</pre>

#Para matar a todos los contenedores
<pre>
docker-compose down
</pre>
Si no llega a funcionar
<pre>
docker stop $(docker ps -aq)
</pre>


#Para loguear sin tener que hacer un docker-compose
<pre>
docker-compose logs -f
</pre>
