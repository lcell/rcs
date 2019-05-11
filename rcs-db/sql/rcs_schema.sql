drop database if exists rcs;
drop user if exists 'rcs'@'%';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database rcs default character set utf8mb4 collate utf8mb4_unicode_ci;
use rcs;
create user 'rcs'@'%' identified by '123456';
grant all privileges on rcs.* to 'rcs'@'%';
flush privileges;
