CREATE TABLE goverment_types (
    id SERIAL PRIMARY KEY,
    type VARCHAR(30) NOT NULL
);

CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    area FLOAT NULL,
    population BIGINT NULL,
	meters_above_sea_level BIGINT NULL,
	timezone FLOAT NULL,
    capital BOOLEAN NULL,
    goverment INT NOT NULL,
    FOREIGN KEY (goverment) REFERENCES goverment_types(id)
);

CREATE TABLE city_human (
    id SERIAL PRIMARY KEY,
    age INT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE CASCADE
);

CREATE TABLE coordinates (
    id SERIAL PRIMARY KEY,
    x DECIMAL NULL,
    y FLOAT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE CASCADE
);

INSERT INTO goverment_types(type)
VALUES('MATRIARCHY'),('MERITOCRACY'),('NOOCRACY'),('PLUTOCRACY');


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE users_city (
    user_id INT NOT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (city_id) REFERENCES city(id) ON DELETE CASCADE
);

INSERT INTO users(username, password)
VALUES('root', '435b41068e8665513a20070c033b08b9c66e4332');
