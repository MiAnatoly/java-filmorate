create table IF NOT EXISTS RATING_MPA
(
    RATING_ID INTEGER              not null,
    RATING    CHARACTER VARYING(5) not null,
    constraint "RATING_MPA_pk"
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    NAME         CHARACTER VARYING(45)  not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                not null,
    RATING_ID    INTEGER                not null,
    constraint "FILMS_pk"
        primary key (FILM_ID),
    constraint "FILMS_RATING_MPA_null_fk"
        foreign key (RATING_ID) references RATING_MPA
);

create table IF NOT EXISTS CATEGORY
(
    CATEGORY    CHARACTER VARYING(45) not null,
    CATEGORY_ID INTEGER               not null,
    constraint "CATEGORY_pk"
        primary key (CATEGORY_ID)
);

create table IF NOT EXISTS FILM_CATEGORY
(
    FILM_ID     INTEGER not null,
    CATEGORY_ID INTEGER not null,
    constraint "FILM_CATEGORY_CATEGORY_null_fk"
        foreign key (CATEGORY_ID) references CATEGORY,
    constraint "FILM_CATEGORY_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILM_CATEGORY_pk"
        primary key (FILM_ID, CATEGORY_ID)
);

create table IF NOT EXISTS USERS_FILMS
(
    USER_ID  INTEGER auto_increment,
    NAME     CHARACTER VARYING(45) not null,
    EMAIL    CHARACTER VARYING(45) not null,
    LOGIN    CHARACTER VARYING(45),
    BIRTHDAY DATE                  not null,
    constraint "USERS_FILMS_pk"
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDSHIP
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint "FRIENDSHIP_USERS_FILMS_USER_ID_fk"
        foreign key (FRIEND_ID) references USERS_FILMS,
    constraint "FRIENDSHIP_USERS_FILMS_null_fk"
        foreign key (USER_ID) references USERS_FILMS,
    constraint "FRIENDSHIP_USERS_FILMS_fk"
        primary key (USER_ID, FRIEND_ID)
);

create table IF NOT EXISTS LIKE_FILM
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "LIKE_FILM_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "LIKE_FILM_USERS_FILMS_null_fk"
        foreign key (USER_ID) references USERS_FILMS,
    constraint "LIKE_FILM_null_fk"
        primary key (USER_ID, FILM_ID)
);

create table IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID   INTEGER auto_increment,
    DIRECTOR_NAME CHARACTER VARYING(30) not null,
    constraint "DIRECTORS_pk"
        primary key (DIRECTOR_ID)
);

create table IF NOT EXISTS FILM_DIRECTOR
(
    FILM_ID     INTEGER not null,
    DIRECTOR_ID INTEGER not null,
    constraint "FILM_DIRECTOR_pk"
        primary key (FILM_ID, DIRECTOR_ID),
    constraint "FILM_DIRECTOR_DIRECTORS_null_fk"
        foreign key (DIRECTOR_ID) references DIRECTORS
            on delete cascade,
    constraint "FILM_DIRECTOR_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade
);