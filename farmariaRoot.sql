-- 1. Asegúrate de que la base de datos exista antes de dar permisos
CREATE DATABASE IF NOT EXISTS farmacia;

-- 2. Refresca los privilegios actuales para evitar conflictos
FLUSH PRIVILEGES;

-- 3. Intenta dar el permiso de nuevo (Asegúrate de las comillas invertidas en el nombre de la DB si es necesario)
GRANT ALL PRIVILEGES ON `farmacia`.* TO 'gerente'@'localhost';

-- 4. Vuelve a refrescar
FLUSH PRIVILEGES;