create database homebanking;
use homebanking;


-- EJECUTAR EL PROYECTO EN VS CODE, DESPUES EJECUTAR LOS INSERST


-- INSERTS para tabla cliente (5 clientes) con DNI solo numéricos
INSERT INTO cliente (dni, nombre, apellido, email) VALUES
('11111111', 'Ana', 'Gomez', 'ana.gomez@mail.com'),
('22222222', 'Luis', 'Perez', 'luis.perez@mail.com'),
('33333333', 'Marta', 'Lopez', 'marta.lopez@mail.com'),
('44444444', 'Jorge', 'Martinez', 'jorge.martinez@mail.com'),
('55555555', 'Laura', 'Fernandez', 'laura.fernandez@mail.com');

-- INSERTS para tabla cuenta_bancaria (10 cuentas, algunos clientes con 2 cuentas)
INSERT INTO cuenta_bancaria (nro_cuenta, saldo, cliente_dni) VALUES
('1001', 1500.50, '11111111'),
('1002', 3200.00, '11111111'),
('1003', 500.00,  '22222222'),
('1004', 7500.25, '22222222'),
('1005', 1000.00, '33333333'),
('1006', 2300.75, '33333333'),
('1007', 12500.00,'44444444'),
('1008', 480.00,  '44444444'),
('1009', 670.50,  '55555555'),
('1010', 8900.00, '55555555');

-- INSERTS para tabla movimiento (20 movimientos variados)
INSERT INTO movimiento (descripcion, fecha_movimiento, monto, nro_cuenta) VALUES
(1, '2025-06-01 09:00:00', 500.00, '1001'),
(2, '2025-06-01 15:30:00', 200.00, '1001'),
(0, '2025-06-02 10:00:00', 100.00, '1002'),
(1, '2025-06-02 11:00:00', 300.00, '1003'),
(2, '2025-06-03 14:20:00', 50.00,  '1004'),
(0, '2025-06-03 16:45:00', 150.00, '1004'),
(1, '2025-06-04 09:15:00', 1000.00,'1005'),
(2, '2025-06-04 12:30:00', 400.00, '1006'),
(0, '2025-06-05 08:00:00', 350.00, '1007'),
(1, '2025-06-05 10:00:00', 600.00, '1008'),
(2, '2025-06-06 09:00:00', 120.00, '1009'),
(1, '2025-06-06 11:30:00', 450.00, '1010'),
(0, '2025-06-07 13:00:00', 700.00, '1001'),
(1, '2025-06-07 15:45:00', 220.00, '1002'),
(2, '2025-06-08 09:30:00', 130.00, '1003'),
(0, '2025-06-08 14:00:00', 80.00,  '1004'),
(1, '2025-06-09 08:15:00', 350.00, '1005'),
(2, '2025-06-09 11:45:00', 200.00, '1006'),
(0, '2025-06-10 10:30:00', 1000.00,'1007'),
(1, '2025-06-10 16:00:00', 500.00, '1008');

-- INSERTS para cuenta_bancaria_historial (20 registros, uno por movimiento)
INSERT INTO cuenta_bancaria_historial (cuenta_bancaria_nro_cuenta, historial_id) VALUES
('1001',1),
('1001',2),
('1002',3),
('1003',4),
('1004',5),
('1004',6),
('1005',7),
('1006',8),
('1007',9),
('1008',10),
('1009',11),
('1010',12),
('1001',13),
('1002',14),
('1003',15),
('1004',16),
('1005',17),
('1006',18),
('1007',19),
('1008',20);