-- 1. Tabla de Usuarios
CREATE TABLE users
(
    id        UUID PRIMARY KEY,
    username  TEXT NOT NULL,
    mail      TEXT NOT NULL UNIQUE,
    pwd       TEXT NOT NULL,
    phone     TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE -- En Postgres usamos BOOLEAN en lugar de 0/1
);

-- 2. Tabla de Géneros
CREATE TABLE genres
(
    id         SERIAL PRIMARY KEY, -- SERIAL reemplaza a AUTOINCREMENT
    genre_name TEXT NOT NULL UNIQUE
);

-- 3. Tabla de Productos
CREATE TABLE products
(
    id           UUID PRIMARY KEY,
    product_name TEXT           NOT NULL,
    artist       TEXT           NOT NULL,
    real_price   DECIMAL(10, 2) NOT NULL,
    sale_price   DECIMAL(10, 2),
    stock        INTEGER DEFAULT 0,
    slug         TEXT UNIQUE    NOT NULL,
    views        INTEGER DEFAULT 0,
    is_visible   BOOLEAN DEFAULT TRUE
);

-- 4. Tabla Intermedia: Productos y Géneros
CREATE TABLE product_genres
(
    product_id UUID REFERENCES products (id) ON DELETE CASCADE,
    genre_id   INTEGER REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, genre_id)
);

-- 5. Tabla de Tracklist
CREATE TABLE tracklists
(
    id         SERIAL PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    song       TEXT NOT NULL
);

-- 6. Tabla de Imágenes de Productos
CREATE TABLE product_images
(
    id         UUID PRIMARY KEY,
    url        TEXT NOT NULL,
    product_id UUID NOT NULL REFERENCES products (id) ON DELETE CASCADE
);

-- 7. Tabla de Wishlist
CREATE TABLE whishlists
(
    user_id    UUID REFERENCES users (id) ON DELETE CASCADE,
    product_id UUID REFERENCES products (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, product_id)
);

-- 8. Tabla de Carrito
CREATE TABLE carts
(
    id         SERIAL PRIMARY KEY,
    user_id    UUID    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    product_id UUID    NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    quantity   INTEGER NOT NULL CHECK (quantity > 0)
);

-- 9. Tabla de Ventas
CREATE TABLE sales
(
    id         SERIAL PRIMARY KEY,
    sale_group UUID    NOT NULL,
    user_id    UUID    NOT NULL REFERENCES users (id),
    product_id UUID    NOT NULL REFERENCES products (id),
    quantity   INTEGER NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() -- Formato estándar de Supabase
);