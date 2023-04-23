
CREATE TABLE IF NOT EXISTS genre (
  genre_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa_rating (
  mpa_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS status (
    status_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
  );

CREATE TABLE IF NOT EXISTS users (
  	user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  	email varchar(320) NOT NULL,
    username varchar(40) NOT NULL,
  	login varchar(40) NOT NULL,
    birthday_date date NOT NULL,
  	friends_id BIGINT NOT NULL
);

 CREATE TABLE IF NOT EXISTS friends (
    friend_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_friend_id BIGINT NOT NULL REFERENCES users(user_id),
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    status_code BIGINT NOT NULL REFERENCES status (status_id)
  );

CREATE TABLE IF NOT EXISTS film (
        film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name varchar(40) NOT NULL,
 	    description varchar(200) NOT NULL,
  	    release_date date NOT NULL,
  	    duration BIGINT NOT NULL,
  	    likes_id BIGINT NOT NULL,
        genre_id BIGINT NOT NULL,
        mpa_id BIGINT NOT NULL REFERENCES mpa_rating (mpa_id)
);

CREATE TABLE IF NOT EXISTS likes (
        like_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        film_id BIGINT NOT NULL REFERENCES film(film_id),
        user_id BIGINT NOT NULL REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    film_id BIGINT NOT NULL REFERENCES film (film_id),
    genre_id  BIGINT NOT NULL REFERENCES genre (genre_id),
    CONSTRAINT film_genre_idx UNIQUE (film_id, genre_id)
);