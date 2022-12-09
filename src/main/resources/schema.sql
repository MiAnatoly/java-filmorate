
create table IF NOT EXISTS  RATING_MPA
(
    RATING_ID INTEGER not null,
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
    NAME        CHARACTER VARYING(45) not null,
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
        foreign key (FILM_ID) references FILMS
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
    STATUS    BOOLEAN,
    constraint "FRIENDSHIP_USERS_FILMS_USER_ID_fk"
        foreign key (FRIEND_ID) references USERS_FILMS,
    constraint "FRIENDSHIP_USERS_FILMS_null_fk"
        foreign key (USER_ID) references USERS_FILMS,
    constraint "FRIENDSHIP_USERS_FILMS_fk"
        UNIQUE (USER_ID, FRIEND_ID)
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
        UNIQUE (USER_ID, FILM_ID)
);