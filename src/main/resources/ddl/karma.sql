-- auto-generated definition
create table karma
(
    user_id        int  not null,
    karma          int  not null,
    karma_credit   int  not null,
    credit_updated date not null,
    constraint karma_user_id_uindex
        unique (user_id)
);

alter table karma
    add primary key (user_id);

