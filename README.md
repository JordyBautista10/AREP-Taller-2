# TALLER 2: DISEÑO Y ESTRUCTURACIÓN DE APLICACIONES DISTRIBUIDAS EN INTERNET

Este proyecto de Java se desarrolló con la intención de abordar las conexiones HTTP (tanto cliente como servidor). Con esto en mente, el código de este proyecto permite usar un cliente JavaScript que puede buscar información acerca de distintas películas, si bien, esto es lo que el cliente experimenta (Una búsqueda sencilla de información relacionada con un filme), realmente lo que se está haciendo es una serie de peticiones tipo HTTP a un servidor fachada creado por este mismo código, una vez recibida la petición el servidor busca en su cache para verificar si ya tiene registro de esa película, en caso de tenerla retorna lo que tiene almacenado, si no tiene registro, realiza la consulta a un API público. La novedad en esta entrega es, que el servidor tiene la capacidad de leer archivos en disco, soporta archivos HTML, CSS, JS e imágenes JPG.

## Para Comenzar

#### Repositorio

En primera instancia, debemos obtener el código del proyecto, por lo que se ejecutara el comando desde consola. (tenga en cuenta que debe estar en la carpeta deseada antes de clonar el repositorio)

~~~
https://github.com/JordyBautista10/AREP-Taller-2.git
~~~

Posteriormente, descargamos las dependencias necesarias y compilamos el código

~~~
mvn clean install compile
~~~

#### Ejecución

Para correr este codigo usando un ID, hay que ingresar a la carpeta que se muestra acontinuacion y ejecutar el archivo llamado Main.java

![image](https://github.com/JordyBautista10/AREP-Taller-2/assets/123812969/01072a26-8b49-465d-aa09-da12f2be0e83)

#### Funcionamiento

Una vez el código esté corriendo debemos poner la siguiente dirección URL en el navegador

~~~
http://localhost:35000/index.html
~~~

Hecho esto, Deberia aparecer una pagian de este estilo

![image](https://github.com/JordyBautista10/AREP-Taller-1/assets/123812969/0b4a4017-a921-4c0a-9842-bcb1d93fd39a)

Únicamente lo que resta es colocar el nombre de la película que se desea buscar, para el siguiente ejemplo se buscara la información de la película, avatar, así qué una vez puesto el nombre de la película se da clic en el botón de Submit

![image](https://github.com/JordyBautista10/AREP-Taller-1/assets/123812969/846b58ce-45de-492c-8e2b-3b16013bc6ec)

Este deberia ser el resultado

![image](https://github.com/JordyBautista10/AREP-Taller-1/assets/123812969/cfd735ef-8d29-4c4d-8208-ae8f45122abf)

Si lo que desea es ver una imagen dentro del servidor, agregué una imagen en la carpeta /resources/public y búsquela por su nombre, en el siguiente ejemplo se añadió la imagen Nitro.jpg, por lo que se busca de la siguiente manera:

~~~
http://localhost:35000/Nitro.jpg
~~~


### Prerequisitos

Para que el código corra de forma satisfactoria y se puedan seguir todos los pasos se necesitara de: Java, Maven y Git; sin embargo,  para la descarga e instalación de estos elementos, adjunto los link de material de apoyo de otros autores

* [Tutorial instalación Java] (https://youtu.be/4WKo13f2Qpc?si=lHG84Jp_k7YbBFmp)
* [Tutorial instalación Git] (https://youtu.be/jpTrSSjPlEo?si=VdcaXSaNEFkR3hCk)
* [Tutorial instalación Maven] (https://youtu.be/biBOXvSNaXg?si=wfySIfBTUERGEVZC)

## Built With

* [Java](http://www.dropwizard.io/1.0.2/docs/) - Lenguaje con el cual funciona la mayor parte del proyecto
* [Html](https://developer.mozilla.org/es/docs/Web/HTML) - Usado para la sección del cliente
* [JavaScript](https://developer.mozilla.org/es/docs/Web/JavaScript) - Este lenguaje le permite al cliente realizar las peticiones necesarias
* [Maven](https://maven.apache.org/) - Usado para la construcción de la estructura del proyecto
* [Git](https://git-scm.com) - Usado para el versionamiento
  
## Versioning

Para el versionamiento se usó [Git](https://git-scm.com). Si necesita volver en alguna versión del código, visite los commits.

## Autor

* **Jordy Santiago Bautista Sepulveda** 
