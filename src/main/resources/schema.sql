create table if not exists karma
(
    user_id        int  not null,
    karma          int  not null,
    karma_credit   int  not null,
    credit_updated date not null,
    primary key (user_id),
    constraint user_id_uindex
        unique (user_id)
);

create table if not exists stats
(
    id       int  not null auto_increment,
    user_id  int  not null,
    stat_day date not null,
    messages int  not null,
    primary key (id),
    constraint user_id_stat_day_uindex
        unique (user_id, stat_day)
);
