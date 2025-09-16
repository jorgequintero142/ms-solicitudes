/***
** db SOLICITUDES
*/ 
/* HU-02 */
CREATE TABLE estado (
    id_estado INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT );

CREATE TABLE tipo_prestamo (
    id_tipo_prestamo INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    monto_minimo NUMERIC(12, 2) NOT NULL,
    monto_maximo NUMERIC(12, 2) NOT NULL,
    tasa_interes NUMERIC(5, 2) NOT NULL );
	

CREATE TABLE solicitud (
    id_solicitud SERIAL PRIMARY KEY,
    monto NUMERIC(12, 2) NOT NULL,
    plazo INTEGER NOT NULL,
    email VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(50) NOT NULL,
    id_estado INTEGER NOT NULL, 
    id_tipo_prestamo INTEGER NOT NULL
	--,CONSTRAINT fk_estado FOREIGN KEY (id_estado) REFERENCES estado(id_estado), 
    --CONSTRAINT fk_tipo_prestamo FOREIGN KEY (id_tipo_prestamo) REFERENCES tipo_prestamo(id_tipo_prestamo)
 );
 
INSERT INTO tipo_prestamo (id_tipo_prestamo, nombre, monto_minimo, monto_maximo, tasa_interes) 
VALUES 
	(1, 'Personal', 500000.00, 50000000.00, 1.78), 
	(2, 'Vivienda', 70000000.00, 999999999.00, 0.87),
	(3, 'Vehicular', 25000000.00, 370000000.00, 1.3), 
	(4, 'Estudio', 450000.00, 10000000.00, 1.45);

INSERT INTO estado (id_estado, nombre, descripcion) 
VALUES 
	 (1,'Pendiente de revisión', 'La solicitud está pendiente de ser revisada.'), 
	 (2,'Aprobado', 'La solicitud ha sido aprobada.'), 
	 (3,'Revisión Manual', 'La solicitud necesita revisión manual.'), 
	 (4,'Rechazado', 'La solicitud ha sido rechazada.');
	 