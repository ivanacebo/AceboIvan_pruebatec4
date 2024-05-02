-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-05-2024 a las 19:36:12
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `agencia`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id` bigint(20) NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id`, `apellido`, `borrado`, `dni`, `email`, `nombre`, `telefono`) VALUES
(1, 'Perez', b'1', '12345678A', 'juan@example.com', 'Juan', '123456789'),
(2, 'López', b'0', '87654321D', 'ana@example.com', 'Ana', '123456789'),
(3, 'Martínez', b'0', '54321678C', 'carlos@example.com', 'Carlos', '654987321');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitacion`
--

CREATE TABLE `habitacion` (
  `id` bigint(20) NOT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `capacidad` int(11) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `disponible_desde` date DEFAULT NULL,
  `disponible_hasta` date DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `numero_habitacion` int(11) DEFAULT NULL,
  `precio_dia` double DEFAULT NULL,
  `tipo_habitacion` varchar(255) DEFAULT NULL,
  `id_hotel` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `habitacion`
--

INSERT INTO `habitacion` (`id`, `borrado`, `capacidad`, `descripcion`, `disponible_desde`, `disponible_hasta`, `estado`, `numero_habitacion`, `precio_dia`, `tipo_habitacion`, `id_hotel`) VALUES
(1, b'1', 2, 'Habitación doble estándar con vista al mar', '2024-06-01', '2024-06-15', 'Disponible', 101, 100, 'Doble', 2),
(2, b'0', 2, 'Habitación doble estándar con vista al mar (actualizada)', '2024-06-10', '2024-06-20', 'Ocupada', 101, 120, 'Doble', 3),
(3, b'0', 1, 'Habitación individual con vista a la ciudad', '2024-06-01', '2024-06-15', 'Disponible', 301, 80, 'Individual', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hotel`
--

CREATE TABLE `hotel` (
  `id` bigint(20) NOT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `ciudad` varchar(255) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `numero_telefono` varchar(255) DEFAULT NULL,
  `pais` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `hotel`
--

INSERT INTO `hotel` (`id`, `borrado`, `ciudad`, `codigo`, `direccion`, `email`, `nombre`, `numero_telefono`, `pais`) VALUES
(1, b'1', 'Buenos Aires', 'HOT001', 'Av. Macacha Güemes 351', 'info@hilton.com.ar', 'Hilton Buenos Aires', '+54 11 4891-0000', 'Argentina'),
(2, b'0', 'Nueva York', 'HOT003', '109 East 42nd Street', 'info@grandhyattnewyork.com', 'Grand Hyatt New York', '+1 212-883-1234', 'Estados Unidos'),
(3, b'0', 'Nueva York', 'HO-5a091b91-5563-4e54-9b24-c7d6dd4b1a7e', 'Fifth Avenue at Central Park South', 'info@theplazahotel.com', 'The Plaza Hotel', '+1 212-759-3000', 'Estados Unidos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva_habitacion`
--

CREATE TABLE `reserva_habitacion` (
  `id` bigint(20) NOT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_reserva` date DEFAULT NULL,
  `precio_total` double DEFAULT NULL,
  `id_cliente` bigint(20) DEFAULT NULL,
  `id_habitacion` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `reserva_habitacion`
--

INSERT INTO `reserva_habitacion` (`id`, `borrado`, `estado`, `fecha_reserva`, `precio_total`, `id_cliente`, `id_habitacion`) VALUES
(1, b'1', 'Confirmada', '2024-08-15', 500, 1, 1),
(2, b'0', 'Pendiente', '2024-09-20', 700, 2, 2),
(3, b'0', 'Confirmada', '2024-11-03', 900, 3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reserva_vuelo`
--

CREATE TABLE `reserva_vuelo` (
  `id` bigint(20) NOT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `estado` varchar(255) DEFAULT NULL,
  `fecha_reserva` date DEFAULT NULL,
  `precio_total` double DEFAULT NULL,
  `id_cliente` bigint(20) DEFAULT NULL,
  `id_vuelo` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `reserva_vuelo`
--

INSERT INTO `reserva_vuelo` (`id`, `borrado`, `estado`, `fecha_reserva`, `precio_total`, `id_cliente`, `id_vuelo`) VALUES
(1, b'1', 'Confirmada', '2024-01-15', 1200, 2, 3),
(2, b'0', 'Pendiente', '2024-01-20', 1350, 2, 2),
(3, b'0', 'Cancelada', '2024-03-25', 1000, 3, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vuelo`
--

CREATE TABLE `vuelo` (
  `id` bigint(20) NOT NULL,
  `aerolinea` varchar(255) DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `cantidad_asientos` int(11) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `destino` varchar(255) DEFAULT NULL,
  `es_directo` varchar(255) DEFAULT NULL,
  `fecha_llegada` date DEFAULT NULL,
  `fecha_partida` date DEFAULT NULL,
  `millas` double DEFAULT NULL,
  `origen` varchar(255) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `tipo_asiento` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `vuelo`
--

INSERT INTO `vuelo` (`id`, `aerolinea`, `borrado`, `cantidad_asientos`, `codigo`, `destino`, `es_directo`, `fecha_llegada`, `fecha_partida`, `millas`, `origen`, `precio`, `tipo_asiento`) VALUES
(1, 'Aerolíneas Ejemplo', b'1', 200, 'ABC123', 'Ciudad B', 'Sí', '2024-05-11', '2024-05-10', 1500.5, 'Ciudad A', 250.75, 1),
(2, 'Avianca', b'0', 180, 'AV456', 'Bogotá', 'No', '2024-07-21', '2024-07-20', 3800, 'Guadalajara', 550, 2),
(3, 'Iberia', b'0', 200, 'IB123', 'Nueva York', 'No', '2024-08-21', '2024-08-20', 3500, 'Londres', 700, 2),
(4, 'Delta Airlines', b'0', 180, 'DL456', 'Tokio', 'Sí', '2024-09-16', '2024-09-15', 9000, 'Los Ángeles', 950, 3);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `habitacion`
--
ALTER TABLE `habitacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnati1hwmlnaiemvjcwuql8web` (`id_hotel`);

--
-- Indices de la tabla `hotel`
--
ALTER TABLE `hotel`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `reserva_habitacion`
--
ALTER TABLE `reserva_habitacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK2p8iqf4knhxbwueiou4xm75ob` (`id_cliente`),
  ADD KEY `FK5ff3yvnmyj0soi2o1dnnyfwem` (`id_habitacion`);

--
-- Indices de la tabla `reserva_vuelo`
--
ALTER TABLE `reserva_vuelo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKt0kirl2xmt8c3dt8whieiomov` (`id_cliente`),
  ADD KEY `FKpauoxdcvff5e2ahpfnqhw4ia4` (`id_vuelo`);

--
-- Indices de la tabla `vuelo`
--
ALTER TABLE `vuelo`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `habitacion`
--
ALTER TABLE `habitacion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `hotel`
--
ALTER TABLE `hotel`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `reserva_habitacion`
--
ALTER TABLE `reserva_habitacion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `reserva_vuelo`
--
ALTER TABLE `reserva_vuelo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `vuelo`
--
ALTER TABLE `vuelo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `habitacion`
--
ALTER TABLE `habitacion`
  ADD CONSTRAINT `FKnati1hwmlnaiemvjcwuql8web` FOREIGN KEY (`id_hotel`) REFERENCES `hotel` (`id`);

--
-- Filtros para la tabla `reserva_habitacion`
--
ALTER TABLE `reserva_habitacion`
  ADD CONSTRAINT `FK2p8iqf4knhxbwueiou4xm75ob` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`),
  ADD CONSTRAINT `FK5ff3yvnmyj0soi2o1dnnyfwem` FOREIGN KEY (`id_habitacion`) REFERENCES `habitacion` (`id`);

--
-- Filtros para la tabla `reserva_vuelo`
--
ALTER TABLE `reserva_vuelo`
  ADD CONSTRAINT `FKpauoxdcvff5e2ahpfnqhw4ia4` FOREIGN KEY (`id_vuelo`) REFERENCES `vuelo` (`id`),
  ADD CONSTRAINT `FKt0kirl2xmt8c3dt8whieiomov` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
