
create table IF NOT EXISTS MPA
(
    ID     INTEGER           not null
        unique,
    RATING CHARACTER VARYING not null,
    constraint "MPA_pk"
        primary key (ID)
);

create table IF NOT EXISTS USERS
(
    USER_ID  INTEGER           not null,
    NAME     CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING not null,
    EMAIL    CHARACTER VARYING not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS GENRE
(
    ID    INTEGER           not null,
    GENRE CHARACTER VARYING not null,
    constraint GENRES_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM
(
    FILM_ID      INTEGER           not null,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING,
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATE         INTEGER,
    RATING_ID    INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILM_MPA_RATING_ID_FK
        foreign key (RATING_ID) references MPA (ID)
);

create table IF NOT EXISTS GENRE_FILM
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint "FILM_GENRE_FILM_null_fk"
        foreign key (FILM_ID) references FILM,
    constraint "FILM_GENRE_GENRE_null_fk"
        foreign key (GENRE_ID) references GENRE
);

create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint LIKE_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint LIKE_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FRIENDSHIP
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    BOOLEAN not null,
    constraint FRIENDSHIP_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDSHIP_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);