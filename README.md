# Prueba-2B-JAVA

<h1>Scrip de la base de datos</h1>

<hr>

Create database Vehiculos;

use Vehiculos;

create table carros(
placa int primary key not null,
marca varchar (25) not null,
modelo varchar (25) not null,
color varchar (25) not null,
uso varchar (25) not null,
gasolina varchar (25) not null,
pais varchar (25) not null
);
