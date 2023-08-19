create table if not exists karma
(
    user_id        bigint not null,
    karma          int    not null,
    karma_credit   int    not null,
    credit_updated date   not null,
    primary key (user_id),
    constraint user_id_uindex
        unique (user_id)
);

create table if not exists stats
(
    id       int    not null auto_increment,
    user_id  bigint not null,
    stat_day date   not null,
    messages int    not null,
    primary key (id),
    constraint user_id_stat_day_uindex
        unique (user_id, stat_day)
);

create table if not exists errepigi
(
    user_id        bigint not null,
    hp             int    not null,
    attaccanti_ids text   not null,
    primary key (user_id),
    constraint user_id_uindex
        unique (user_id)
);

create table if not exists utonti
(
    user_id    bigint not null,
    first_name text   not null,
    last_name  text,
    user_name  text,
    updated    date   not null,
    primary key (user_id),
    constraint user_id_uindex
        unique (user_id)
);