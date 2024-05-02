# 	SISTEMA PARA LA GESTIÓN DE RESERVAS DESDE UNA AGENCIA

##	Descripción del proyecto

Este proyecto consiste en la creación de una API con springboot desde la que se lleva a cabo el CRUD de reservas para un hotel (habitación) y un vuelo.

## Modelo de capas

**Model:** Donde encontramos el modelo de nuestra api. Aquí describimos las clases que vamos a implementar. **Repository:** La unión de nuestro proyecto api con nuestra base de datos. **Service:** Llevamos a cabo 
la lógica de negocio de nuestra API. **DTO:** Modificamos los datos que queremos mostrar o guardar con nuevas clases. **Controller:** El controlador de salida de nuestra API. punto de conexión con la parte front.
**Config:** La parte de configuración de nuestra API, en este caso la parte se seguridad. **Test:** La parte de pruebas unitarias que tiene que pasar nuestra API para comprobar si es funcional.

## Explicación funcionamiento del proyecto

**Model -> ** El modelo usado se basa en un total de 6 clases. Por un lado tenemos Hotel unido a Habitacion (muchas habitaciones en un hotel) y habitación unido a ReservaHabitación 
 (una habitacion muchas reservas) y la reserva de la habitación esta unida a cliente(una reserva un cliente).Por otro lado tenemos la parte de Vuelo que esta unido a reserva de vuelos (un vuelo muchas reservas)
 y la reserva de vuelos esta unido a cliente(un cliente muchas reservas tanto de vuelos como de habitaciones).

**Repository ->** Aqui se generan consultas como busqueda de lo que no esta borrado por logica aplicada entre otras. 

**DTO -> ** Modificación aplicada a los datos que quiero sobre el modelo de mis clases.

**Service -> ** Cada clase tiene un service propio donde realizamos el CRUD. Se añade en vuelos y hoteles una busqueda propia requerida por rango de fechas y pais.

**Controller ->** Cada clase tiene un controller propio donde realizamos el CRUD a nivel de unión con la parte front. (pruebas de postman para nosotros).

**Config ->** Lógica donde dejamos que todos los usuarios puedan hacer busquedas de tipo (GET) ya sean listas o la busqueda sobre un id. Tambien se permiten busquedas  a todos los usuarios sobre un rango de fechas y pais.
		La creación de reservas de una habitación y de un vuelo.
		
** Test ->** Se genera algún test sobre el controlador de vuelos y habitaciones y en el service sobre el hotel.

## Explicación de la lógica por modelo de capas

** Controller **
	Cada controller genera una inyeccion de instancia de la interfaz creada en service y lleva a cabo un CRUD sobre GET, PUT, POST, DELETE comprobando el codigo de respuesta y mandando una respuesta propia.
	Se genera documentación propia de SWAGER con una estructura bien detallada de cada codigo de respuesta y una descripcion de cada metodo.
	
** Service **
	Se genera una interfaz por cada modelo que tenemos y una clase que implemente dicha interfaz. Aqui generamos la lógica de negocio por lo que es donde más codigo se desarrolla. Se genera un CRUD con lógica propia para 
	cada modelo. Descripción:
	-> Todos los metodos tienen una descripción detallada de su funciónamiento.
	1. El save -> Se intenta siempre hacer comprobaciones para que no se repitan los datos introducidos por ejemplo un codigo par el hotel (si no se da se genera uno aleatorio), el dni para el cliente...
	2. Listas sobre datos -> Se buscan todos los datos no borrados de la base de datos y se traen en una lista con un DTO para enseñar los datos que yo quiera.
	3. Busqueda por su identificador -> El id va a ser el identificador de cada modelo. Hacemos una busqueda por ese id y comprobamos que no este borrado anteriormente.
	4. Delete -> Para cada modelo se implementa una logica de borrado. Se tiene en cuenta que no se puede borrar un dato si tiene otros datos asociados. (Ejemplo: hotel si tiene habitaciones asociadas no borrarlo.
				Si ya esta borrado anteriormente mandar una excepción.
	5. Edit de datos -> Para editar los datos se busca que no este borrado anteriormente mandando asi una excepción y se procede a su edición con un modelo propio DTO. En algunos modelos se añade lógica de comprobación
				necesario antes de poder llevar a cabo el edit. (Ejemplo: reserva de vuelos se verifica que no este borrado el vuelo ni el usuairo antes de su edición y las fechas sean acordes).
	
## Supuestos técnicos

1. Se incorporan varias consusltas a los repositorios para traer datos acordes de uso necesario para los crud.
2. Se implementa el borrado lógico con un campo booleano en cada Clase utilizada.
3. Se asegura que dicho borrado lógico no se pueda llevar a cabo si tiene datos anidados
4. Se manejan excepciones.
5. Filtros personalizados para habitaciones y vuelos por rango de fechas y pais en concreto
6. Se tiene en cuenta que los datos son correctos los enviados para la creación puesto que esto se puede hacer perfectamente desde el front.
7. Realización de algun test para la comprobación de algun funcionamiento del service y del controller.
8. Se añade seguridad en todo excepto para las busquesdas de tipo get y la creacion de los dos tipos de reserva.
9. Se trabaja con Streams lamdas optionals


## Distintos escenarios considerados

**1 -> Eliminación lógica por ID;**
		Se adquiere el id visualizandoun dato en concreto.
		
**2 -> Inmutabilidad del ID en la base de datos;**
		Se garantiza que el id nunca puede ser modificado de ningua de las clases que utilizamos ya que es su identificador.
		
**3 -> Prevención de duplicación de datos:**
		Añadimos logica para asegurar la no duplicación de datos.
	
**4 -> Unión Bidireccional en base de datos**

**5 -> CRUD propio para cada modelo**

## Relación entre clases
	
Se adjunta un UML.
La clase hotel esta unida a Habitación de tal forma que un hotel puede tener muchas habitaciones.
La clase Habitación esta unida a reserva de habitaciones de tal forma que una habitación puede tener muchas reservas.
la clase Vuelo esta unida a reserva de vuelos de tal forma que un vuelo puede tener muchas reservas.
La clase Reserva habitación y reserva vuelo esta unido a clientes de tal forma que un cliente puede tener muchas reservas.

## Otros escenarios considerados

* Se tiene en cuenta que desde el front se puede comprobar si los datos introducidos por el cliente son los acordes con el formato esperado.
* Se trabaja con JSON.

##Requisitos del sistema

- Kit de desarrollo Java (JDK) instalado en el sistema.
- Acceso a una base de datos relacional compatible (por ejemplo, MySQL, PostgreSQL, phpMyAdmin). Se agrega archivo.sql con los datos de prueba del sistema.
- Se agrega collección de postam con las url de prueba.
- Servidor de aplicaciones compatible: Se requiere un servidor de aplicaciones compatible.
- Instalación y configuración
- Clonar el repositorio desde GitHub.
- Configure la conexión a la base de datos en el archivo de configuración correspondiente.
- Configuración del proyecto y ejecución desde un IDE de desarrollo. (sugerencia -> intelliJ)

##Contribuciones

Las contribuciones al proyecto son bienvenidas. Se agradece cualquier corrección o sugerencia para mejorar el sistema.
