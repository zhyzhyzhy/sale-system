drop table if exists user;
create table user
(
  id       int primary key auto_increment,
  name     varchar(32)  not null,
  password varchar(128) not null,
  role     tinyint      not null,
  unique key uk_name (name)
)
  engine = InnoDB
  DEFAULT CHARSET = UTF8
  comment '用户信息表';

drop table if exists good;
create table good
(
  id      int primary key auto_increment,
  user_id int           not null,
  title   varchar(128)  not null,
  summary varchar(256)  not null,
  image   varchar(1024) not null,
  detail  varchar(1024) not null,
  price   decimal       not null,
  unique key uk_tile (title)
) engine = InnoDB
  DEFAULT CHARSET = UTF8
  comment '商品表';

drop table if exists trans_record;
create table trans_record
(
  id      int primary key auto_increment,
  user_id int not null,
  good_id int not null,
  has_buy bit not null ,
  num     int not null,
  unique key uk_user_good (user_id, good_id),
  index id_has_buy(user_id, has_buy),
  index id_has_sell(good_id, has_buy)
) engine = InnoDB
  DEFAULT CHARSET = UTF8
  comment '交易表';