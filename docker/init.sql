create user 'sgrunt'@localhost identified by 'sgrunt';
grant all privileges on sgrunt.* to 'sgrunt'@'%' identified by 'sgrunt';
flush privileges;
