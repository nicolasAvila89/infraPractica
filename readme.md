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


#Levantar los contenedores quitar el -d si se quiere sacar el modo daemon
<pre>
docker run -d -e &quot;SPRING_PROFILES_ACTIVE=prod&quot; -e JAVA_OPTS=&apos;-Xmx400&apos; -e ENV_NAME=&quot;Server1&quot; -p 8080:8080 -t springio/gs-spring-boot-docker
docker run -d -e &quot;SPRING_PROFILES_ACTIVE=prod&quot; -e JAVA_OPTS=&apos;-Xmx400&apos; -e ENV_NAME=&quot;Server2&quot; -p 8081:8080 -t springio/gs-spring-boot-docker
docker run -d -e &quot;SPRING_PROFILES_ACTIVE=prod&quot; -e JAVA_OPTS=&apos;-Xmx400&apos; -e ENV_NAME=&quot;Server3&quot; -p 8082:8080 -t springio/gs-spring-boot-docker
</pre>

#Para matar a todos los contenedores
docker stop $(docker ps -aq)

#Para loguear sin tener que hacer un docker-compose
<pre>
for c in $(docker ps -a --format="{{.Names}}")
do
       docker logs -f $c > /tmp/$c.log 2> /tmp/$c.err &
done
tail -f /tmp/*.{log,err}
</pre>
