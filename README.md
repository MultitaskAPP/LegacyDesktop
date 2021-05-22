# MULTITASK | DESKTOP
Repositorio de la aplicacion de escritorio del poryecto Multitask <br>
Aplicación para plataformas WINDOWS / LINUX y MACOS pensada para ser una plataforma que engloba multiples herramientas de organizacion y trabajo grupal. <br><br>
El usuario puede crear TABLEROS con diferentes apartados y secciones en las que puede añadir TAREAS, también permite la creación de EVENTOS a un calendario con recordatorios y notificaciones. El usuario también tiene la capacidad de añadir NOTAS en las que puede apuntar todo tipo de información y tenerla siempre a mano. Todo ello visible desde una vista RESUMEN para tener toda la información necesaria siempre a primera vista.

Multitask además incluye la funcionalidad de añadir CONTACTOS y GRUPOS con los que poder CHATEAR y COMPARTIR todas las funcionalidades anteriormente mencionadas. Cada usuario dispone de una NUBE con 1GB de almacenamiento gratuito y diferentes planes con las que poder aumentar el espacio de esta.

<br>

![MainFrame](https://i.imgur.com/M2UhCBq.jpg)

<br>

## ESPECIFICACIONES TECNICAS:
 - Desarrollado en JavaFX
 - Patrón de diseño DAO
 - Conexion con API en HEROKU mediante metodos de peticion HTML
  - Base de datos MySQL con hosteo en la nube y llamadas desde API.
 - Encriptación de datos mediante SHA-256 y JWT
 - Implementación de CloudinaryAPI para almacenamiento en la nube y hosteo de imagenes de perfil
 - Implementación de ImageTweakerTool para las imagenes de perfil.
 - Uso de JSON para lectura y escritura de datos.

<br>

 ## FUNCIONALIDADES:
  - ### GENERAL:
    - Personalización de la interfaz con las siguientes caracteristicas:
        - Tema CLARO / OSCURO
        - Color identificativo del usuario: Según el color seleccionado diferentes partes de la interfaz se adaptan al color escogido.
    - Creación de GRUPOS con multiples usuarios, estos tendrán las mismas herramientas que los USUARIOS pero sus datos se comparten con todos los USUARIOS que pertenezcan a este.
    - Posibilidad de añadir CONTACTOS con los que poder chatear y compartir información.
  - ### DASHBOARD:
    - Interfaz unica y minimalista pensada para tener toda la información clara y a la vista
    - Historial de NOTIFICACIONES ordenadas de más reciente a más antigual
    - Vista de los proximos 5 EVENTS a 30 dias vista
    - Se muestran las 10 primeras TASKS ordenadas de más reciente a más antigua que no esten finalizadas.
  - ### SCHEDULES:
    - Implementación de CRUD
    - Dos formas disponibles para la visualización de datos
        - LIST VIEW: Todas las TASKS de un SCHEDULE ordenadas por prioridad y por fecha de creación, de más nuevas a más antiguas.
        - SCHEDULE VIEW: Separación de las TASKS según la LIST a la que pertenzcan, por cada LIST se añade una columna y dentro de ella todas las TASKS de esta.
    - Personalización del SCHEDULE con color identificativo a eleccion del creador.
    - Dialogo especifico para la creación, edición y visualización detallada de un SCHEDULE.
 - ### TASKS:
    - Implementación de CRUD
    - Añadir fecha de finalización de una TASK
    - Establecer la prioridad de una TASK
    - Establecer la duración de una TASK en horas
    - Cambiar el estado de una TASK si esta finalizada o no, sin eliminarla del tablero.
    - Dialogo personalizado para la creación, edición y visualización en detalle de la TASK.
 - ### EVENTS:
    - Implementación de CRUD
    - Interfaz "PAGE VIEW" para la visualización de los EVENTS sin perder el calendario y con la información del evento util visible a primera vista.
    - Posibilidad de añadir fecha de finalización a un EVENT
    - Los EVENTS pueden durar todo el dia o de una franja horaria a otra.
    - Opción para marcar un EVENT una repetición semanal, menusal o anual.
    - Dialogo personalizado para la creación, edición y visualización en detalle del EVENT.
 - ### NOTES:
    - Implementación de CRUD
    - Interfaz "FLOWLAYOUT" para tener todas las NOTES siempre visibles
    - Dialogo personalizado para la creación, edición y visualización en detalle de la NOTE.



