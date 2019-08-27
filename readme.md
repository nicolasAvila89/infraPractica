# Para correr la app de prueba
./gradlew build && java -jar build/libs/gs-spring-boot-docker-0.1.0.jar

# Para buildear el docker con la version de la app
./gradlew build docker

# Para correr un container con 400Mb de memoria
docker run -e "SPRING_PROFILES_ACTIVE=prod" -e JAVA_OPTS='-Xmx400' -p <host_port>:8080 -t springio/gs-spring-boot-docker

# Enpoints de la app 
	http://localhost:8080/ (info de la memoria)
	http://localhost:8080/markers/purchase(suma de a 100Mb de memoria)
	http://localhost:8080/markers(para saber la cantidad de marcadores)

# Configurar NGINX
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

# Configurar HAProxy
NGINX soporta sticky session solo para su version plus y es paga, como alternativa podemos instalar HAProxy
https://upcloud.com/community/tutorials/haproxy-load-balancer-ubuntu/
https://thisinterestsme.com/haproxy-sticky-sessions/
Copiamos en el archivo de configuración lo siguiente
<pre>
frontend http_front
   bind *:8091
   stats uri /haproxy?stats
   default_backend http_back

backend http_back
   balance roundrobin
   #server Server1 localhost:8080 check
   #server Server2 localhost:8081 check
   #server Server3 localhost:8082 check
   cookie SRVNAME insert
   server Server1 localhost:8080 cookie S1 check
   server Server2 localhost:8081 cookie S2 check
   server Server3 localhost:8082 cookie S3 check  
</pre> 
En esta configuración esta activado sticky session descomentar las lineas y comentar las de abajo para desactivarlo

# Configurar Halzecast para session replication
https://dzone.com/articles/spring-boot-hazelcast-for-session-replication  
Un link un poco mas practico
https://www.baeldung.com/java-hazelcast
Importante: al setear la configuración poner multicast para que se descubran automaticamente los nodos
Para monitorear la replicacion se puede acceder a monitor de Hazelcast desde
http://127.0.0.1:8070/hazelcast-mancenter/
el monitor levanta automaticamente desde docker-compose
en app.properties siempre modificar por la ip fisica de tu maquina y no poner nunca 127.0.0.1

# Trabajar con los contenedores
Esto me permite levantar el entorno y subir y bajar contenedores puntuales para hacer las pruebas
<pre>
docker-compose up
docker-compose down
docker-compose rm -f -s -v server1
docker-compose up server1
</pre>

# Para matar a todos los contenedores
<pre>
docker-compose down
</pre>
Si no llega a funcionar
<pre>
docker stop $(docker ps -aq)
</pre>

# Para loguear sin tener que hacer un docker-compose
<pre>
docker-compose logs -f
</pre>


# Guia práctica del ejercicio
<ol>

<li>Levanto un solo contenedor server1 sin sesion, muestro que no puedo storear nada. Configurar app.properties en sessionEnabled=false</li>
<li>Habilito la sesion, muestro que ahora guarda y empiezo a comprar marcadores. El servidor se cae por el consumo de memoria.</li>
<li>Solucion al problema, poner mas servidores. Como hago? load balancer. Contar un poco como levantar el load balancer</li>
<li>Hacer un test con jmeter y mostrar los logs.Tirar el log con docker-compose logs -f. Ver que esta distribuyendo la carga entre todos los servidores</li>
<li>Mostrar ahora la que pasa con la session cuando accedo sin sticky, mostrar que pierdo los datos entre request y request.</li>
<li>Activar Sticky ahora, mostrar que siempre voy contra el mismo server. Todo bien no?? NO</li>
<li>Bajar el server donde esta mi session... que paso? perdi todo. Como lo soluciono?</li>
<li>Activar Session Replication desde HazelcastEnabled=true, contar de que se trata</li>
<li>Bajar de nuevo el server. Mostrar que con NGINX sin sticky tambien mantiene la session pero pegandole a distintos servers.</li>
<li>Mostrar monitor de Hazelcast como una herramienta de monitoreo </li>
<li>Scaling, contar un poco de esto</li>
</ol>
