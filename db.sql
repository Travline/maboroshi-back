-- Habilitar el soporte de claves foráneas
PRAGMA foreign_keys = ON;

-- 1. Tabla de Usuarios
CREATE TABLE users (
    id TEXT PRIMARY KEY, -- UUID
    username TEXT NOT NULL,
    mail TEXT NOT NULL UNIQUE,
    pwd TEXT NOT NULL,
    phone INTEGER,
    is_active INTEGER DEFAULT 1 CHECK (is_active IN (0, 1))
);

-- 2. Tabla de Géneros
CREATE TABLE genres (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    genre_name TEXT NOT NULL UNIQUE
);

-- 3. Tabla de Productos
CREATE TABLE products (
    id TEXT PRIMARY KEY, -- UUID
    product_name TEXT NOT NULL,
    artist TEXT NOT NULL,
    real_price DECIMAL(10, 2) NOT NULL,
    sale_price DECIMAL(10, 2),
    stock INTEGER DEFAULT 0,
    slug TEXT UNIQUE NOT NULL,
    views INTEGER DEFAULT 0,
    is_visible INTEGER DEFAULT 1 CHECK (is_visible IN (0, 1))
);

-- 4. Tabla Intermedia: Productos y Géneros (Relación N:M)
CREATE TABLE product_genres (
    product_id TEXT,
    genre_id INTEGER,
    PRIMARY KEY (product_id, genre_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

-- 5. Tabla de Tracklist (Lista de canciones por producto)
CREATE TABLE tracklists (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_id TEXT NOT NULL,
    song TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 6. Tabla de Imágenes de Productos
CREATE TABLE product_images (
    id TEXT PRIMARY KEY, -- UUID
    url TEXT NOT NULL,
    product_id TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 7. Tabla de Guardados / Wishlist (Relación N:M entre Users y Products)
CREATE TABLE whishlists (
    user_id TEXT,
    product_id TEXT,
    PRIMARY KEY (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 8. Tabla de Carrito
CREATE TABLE carts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id TEXT NOT NULL,
    product_id TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 9. Tabla de Ventas (Historial de compras)
CREATE TABLE sales (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sale_group TEXT NOT NULL, -- UUID para agrupar productos en una misma compra
    user_id TEXT NOT NULL,
    product_id TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);