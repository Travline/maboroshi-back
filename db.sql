-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.users (
    id uuid NOT NULL,
    username text NOT NULL,
    mail text NOT NULL UNIQUE,
    pwd text NOT NULL,
    phone text,
    is_active boolean DEFAULT true,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
CREATE TABLE public.genres (
    id integer NOT NULL DEFAULT nextval('genres_id_seq'::regclass),
    genre_name text NOT NULL UNIQUE,
    CONSTRAINT genres_pkey PRIMARY KEY (id)
);
CREATE TABLE public.products (
    id uuid NOT NULL,
    product_name text NOT NULL,
    artist text NOT NULL,
    real_price numeric NOT NULL,
    sale_price numeric,
    stock integer DEFAULT 0,
    slug text NOT NULL UNIQUE,
    views integer DEFAULT 0,
    is_visible boolean DEFAULT true,
    type text NOT NULL DEFAULT 'vinyl'::text,
    status text NOT NULL DEFAULT 'sale'::text,
    artist_id uuid,
    CONSTRAINT products_pkey PRIMARY KEY (id),
CONSTRAINT products_artist_id_fkey FOREIGN KEY (artist_id) REFERENCES public.artists(id)
);
CREATE TABLE public.product_genres (
   product_id uuid NOT NULL,
   genre_id integer NOT NULL,
   CONSTRAINT product_genres_pkey PRIMARY KEY (product_id, genre_id),
   CONSTRAINT product_genres_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id),
   CONSTRAINT product_genres_genre_id_fkey FOREIGN KEY (genre_id) REFERENCES public.genres(id)
);
CREATE TABLE public.tracklists (
    id integer NOT NULL DEFAULT nextval('tracklists_id_seq'::regclass),
    product_id uuid NOT NULL,
    song text NOT NULL,
    CONSTRAINT tracklists_pkey PRIMARY KEY (id),
    CONSTRAINT tracklists_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id)
);
CREATE TABLE public.product_images (
    id uuid NOT NULL,
    url text NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT product_images_pkey PRIMARY KEY (id),
    CONSTRAINT product_images_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id)
);
CREATE TABLE public.wishlists (
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT wishlists_pkey PRIMARY KEY (user_id, product_id),
    CONSTRAINT wishlists_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT wishlists_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id)
);
CREATE TABLE public.carts (
    id integer NOT NULL DEFAULT nextval('carts_id_seq'::regclass),
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity integer NOT NULL CHECK (quantity > 0),
    CONSTRAINT carts_pkey PRIMARY KEY (id),
    CONSTRAINT carts_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT carts_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id)
);
CREATE TABLE public.sales (
    id integer NOT NULL DEFAULT nextval('sales_id_seq'::regclass),
    sale_group uuid NOT NULL,
    user_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity integer NOT NULL CHECK (quantity > 0),
    created_at timestamp with time zone DEFAULT now(),
    CONSTRAINT sales_pkey PRIMARY KEY (id),
    CONSTRAINT sales_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT sales_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id)
);
CREATE TABLE public.artists (
    id uuid NOT NULL,
    name text NOT NULL UNIQUE,
    image text NOT NULL,
    CONSTRAINT artists_pkey PRIMARY KEY (id)
);
CREATE TABLE public.product_images_backup (
    id uuid,
    url text,
    product_id uuid
);