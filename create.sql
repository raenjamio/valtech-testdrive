create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=InnoDB
create table hibernate_sequence (next_val bigint) engine=InnoDB
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=InnoDB
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=MyISAM
create table hibernate_sequence (next_val bigint) engine=MyISAM
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=MyISAM
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=MyISAM
alter table reservation add constraint UK12sugik6cgvwk25pcw85e3ttf unique (date_departure, car_id)
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=MyISAM
create table hibernate_sequence (next_val bigint) engine=MyISAM
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=MyISAM
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=MyISAM
alter table reservation add constraint UK12sugik6cgvwk25pcw85e3ttf unique (date_departure, car_id)
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=MyISAM
create table hibernate_sequence (next_val bigint) engine=MyISAM
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=MyISAM
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=MyISAM
alter table reservation add constraint UK12sugik6cgvwk25pcw85e3ttf unique (date_departure, car_id)
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
create table car (id bigint not null, brand varchar(255), description varchar(255), primary key (id)) engine=MyISAM
create table hibernate_sequence (next_val bigint) engine=MyISAM
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
insert into hibernate_sequence values ( 1 )
create table reservation (id bigint not null, date_arrival datetime not null, date_departure datetime not null, price decimal(5,2), state varchar(255), car_id bigint not null, user_id bigint not null, primary key (id)) engine=MyISAM
create table user (id bigint not null, email varchar(255), last_name varchar(255), name varchar(255), primary key (id)) engine=MyISAM
alter table reservation add constraint UK12sugik6cgvwk25pcw85e3ttf unique (date_departure, car_id)
alter table reservation add constraint FKgkmbspv7rljixxoxo1af80lpp foreign key (car_id) references car (id)
alter table reservation add constraint FKm4oimk0l1757o9pwavorj6ljg foreign key (user_id) references user (id)
