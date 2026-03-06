USE farmacia;

CREATE TABLE medicamento(
    id_medicamento INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    compuesto VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);


CREATE TABLE usuario(
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
rol ENUM('admin', 'user') NOT NULL,
nombre VARCHAR(255) NOT NULL,
contrasena VARCHAR(255) NOT NULL,
estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE venta (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unidad DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);


-- Agregar Medicamento
DELIMITER $$

CREATE PROCEDURE AgregarMedicamento(
    IN p_nombre VARCHAR(100),
    IN p_compuesto VARCHAR(100),
    IN p_precio DECIMAL(10,2),
    IN p_cantidad INT
)
BEGIN
    INSERT INTO medicamento(nombre, compuesto, precio, cantidad, estado)
    VALUES (p_nombre, p_compuesto, p_precio, p_cantidad, TRUE);
END$$

DELIMITER ;

-- Eliminar Medicamento(Borrado logico)
DELIMITER $$

CREATE PROCEDURE EliminarMedicamento(
    IN p_id INT
    )
BEGIN
    UPDATE medicamento
    SET estado = FALSE
    WHERE id_medicamento = p_id;
END$$

DELIMITER ;


-- Cambiar el Nombre de un Medicamento
DELIMITER $$
CREATE PROCEDURE CambiarNombreMedicamento(
IN p_id INT,
IN p_nuevo_nombre VARCHAR(100)
) 

BEGIN 

	IF (SELECT COUNT(*) FROM medicamento WHERE id_medicamento = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del medicamento no existe';
    END IF;
    
    UPDATE medicamento
    SET nombre = p_nuevo_nombre
    WHERE id_medicamento = p_id;
END $$
DELIMITER ;

-- Cambiar el Compuesto de un Medicamento
DELIMITER $$
CREATE PROCEDURE CambiarCompuestoMedicamento(
IN p_id INT,
IN p_nuevo_compuesto VARCHAR(100)
) 

BEGIN 

	IF (SELECT COUNT(*) FROM medicamento WHERE id_medicamento = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del medicamento no existe';
    END IF;
    
    UPDATE medicamento
    SET compuesto = p_nuevo_compuesto
    WHERE id_medicamento = p_id;
END $$
DELIMITER ;


-- Cambiar el Precio de un Medicamento
DELIMITER $$
CREATE PROCEDURE CambiarPrecioMedicamento(
IN p_id INT,
IN p_nuevo_precio DECIMAL(10,2)
) 

BEGIN 

	IF (SELECT COUNT(*) FROM medicamento WHERE id_medicamento = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del medicamento no existe';
    END IF;
    
    UPDATE medicamento
    SET precio = p_nuevo_precio
    WHERE id_medicamento = p_id;
END $$
DELIMITER ;


-- Cambiar la Cantidad de un Medicamento
DELIMITER $$
CREATE PROCEDURE CambiarCantidadMedicamento(
IN p_id INT,
IN p_nueva_cantidad INT
) 

BEGIN 

	IF (SELECT COUNT(*) FROM medicamento WHERE id_medicamento = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del medicamento no existe';
    END IF;
    
    UPDATE medicamento
    SET cantidad = p_nueva_cantidad
    WHERE id_medicamento = p_id;
END $$
DELIMITER ;


-- Agregar Usuario
DELIMITER $$

CREATE PROCEDURE AgregarUsuario(
    IN p_rol ENUM('admin','user'),
    IN p_nombre VARCHAR(255),
    IN p_contrasena VARCHAR(255)
)
BEGIN
    INSERT INTO usuario(rol, nombre, contrasena, estado)
    VALUES (p_rol, p_nombre, p_contrasena, TRUE);
END$$

DELIMITER ;


-- Eliminar Usuario(Borrado logico)
DELIMITER $$

CREATE PROCEDURE EliminarUsuario(
    IN p_id INT
    )
BEGIN
    UPDATE usuario
    SET estado = FALSE
    WHERE id_usuario = p_id;
END$$

DELIMITER ;


-- Cambiar el Nombre de un Usuario
DELIMITER $$
CREATE PROCEDURE CambiarNombreUsuario(
IN p_id INT,
IN p_nuevo_nombre VARCHAR(255)
) 

BEGIN 

	IF (SELECT COUNT(*) FROM usuario WHERE id_usuario = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del Usuario no existe';
    END IF;
    
    UPDATE usuario
    SET nombre = p_nuevo_nombre
    WHERE id_usuario = p_id;
END $$
DELIMITER ;


-- Cambiar la Contrasena de un Usuario 
DELIMITER $$
CREATE PROCEDURE CambiarContrasenaUsuario(
IN p_id INT,
IN p_nueva_contrasena VARCHAR(255)
) 

BEGIN 

	IF (SELECT COUNT(*) FROM usuario WHERE id_usuario = p_id) = 0 THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El ID del Usuario no existe';
    END IF;
    
    UPDATE usuario
    SET contrasena = p_nueva_contrasena
    WHERE id_usuario = p_id;
END $$
DELIMITER ;

-- Agregar Venta
DELIMITER $$

CREATE PROCEDURE AgregarVenta(
    IN p_producto_id INT,
    IN p_cantidad INT
)
BEGIN
    DECLARE v_precio DECIMAL(10,2);
    DECLARE v_total DECIMAL(10,2);

    -- Obtener precio del medicamento
    SELECT precio INTO v_precio
    FROM medicamento
    WHERE id_medicamento = p_producto_id AND estado = TRUE;

    -- Calcular total
    SET v_total = v_precio * p_cantidad;

    -- Insertar venta
    INSERT INTO venta(producto_id, cantidad, precio_unidad, total, fecha, estado)
    VALUES (p_producto_id, p_cantidad, v_precio, v_total, NOW(), TRUE);

    -- Descontar stock
    UPDATE medicamento
    SET cantidad = cantidad - p_cantidad
    WHERE id_medicamento = p_producto_id;
    
END$$

DELIMITER ;


-- Cancelar Venta
DELIMITER $$

CREATE PROCEDURE CancelarVenta(
    IN v_id INT
)
BEGIN
	DECLARE v_cantidad INT;
    DECLARE p_id INT;

    -- Obtener Id y Stock del medicamento
    SELECT producto_id, cantidad INTO p_id, v_cantidad
    FROM venta
    WHERE id_venta = v_id AND estado = TRUE;

	-- Validar que exista y no esté cancelada
    IF p_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La venta no existe o ya está cancelada';
    END IF;
    
    -- Aumentar stock
    UPDATE medicamento
    SET cantidad = cantidad + v_cantidad
    WHERE id_medicamento = p_id;
    
    -- Cncelar venta
    UPDATE venta
    SET estado = FALSE
    WHERE id_venta = v_id;
END$$

DELIMITER ;


-- Verifica disponibilidad del medicamento antes de ingresar una venta
DELIMITER $$

CREATE TRIGGER verificarDisponibilidad
BEFORE INSERT ON venta
FOR EACH ROW
BEGIN
    DECLARE estado_actual BOOLEAN;
    DECLARE stock_actual INT;

    -- Obtener estado y stock actual del medicamento
    SELECT estado, cantidad INTO estado_actual, stock_actual
    FROM medicamento
    WHERE id_medicamento = NEW.producto_id;

	-- Validar si el producto existe
	IF estado_actual IS NULL THEN
		SIGNAL SQLSTATE '45000'
		SET MESSAGE_TEXT = 'El medicamento no existe';
	END IF;
    
    -- Validar si el producto esta activo
    IF estado_actual = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Medicamento no disponible';
    END IF;
    
    -- Validar si hay suficiente stock
    IF stock_actual < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente para realizar la venta';
    END IF;

END$$

DELIMITER ;

-- Verifica disponibilidad del medicamento antes de actualizar informacion
DELIMITER $$

CREATE TRIGGER verificarDisponibilidadActualizacion
BEFORE UPDATE ON medicamento
FOR EACH ROW
BEGIN
    
    -- Si está inactivo y sigue inactivo, no permitir cambios
    IF OLD.estado = FALSE AND NEW.estado = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Medicamento no disponible';
    END IF;

END$$

DELIMITER ;


