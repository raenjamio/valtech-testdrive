## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run --name mysqldb -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d mysql

# connect to mysql and run as root user
#Create Databases
CREATE DATABASE test_drive_dev;
CREATE DATABASE test_drive_prod;

#Create database service accounts
CREATE USER 'test_drive_dev_user'@'localhost' IDENTIFIED BY 'test_drive';
CREATE USER 'test_drive_prod_user'@'localhost' IDENTIFIED BY 'test_drive';
CREATE USER 'test_drive_dev_user'@'%' IDENTIFIED BY 'test_drive';
CREATE USER 'test_drive_prod_user'@'%' IDENTIFIED BY 'test_drive';

#Database grants
GRANT SELECT ON test_drive_dev.* to 'test_drive_dev_user'@'localhost';
GRANT INSERT ON test_drive_dev.* to 'test_drive_dev_user'@'localhost';
GRANT DELETE ON test_drive_dev.* to 'test_drive_dev_user'@'localhost';
GRANT UPDATE ON test_drive_dev.* to 'test_drive_dev_user'@'localhost';
GRANT SELECT ON test_drive_prod.* to 'test_drive_prod_user'@'localhost';
GRANT INSERT ON test_drive_prod.* to 'test_drive_prod_user'@'localhost';
GRANT DELETE ON test_drive_prod.* to 'test_drive_prod_user'@'localhost';
GRANT UPDATE ON test_drive_prod.* to 'test_drive_prod_user'@'localhost';
GRANT SELECT ON test_drive_dev.* to 'test_drive_dev_user'@'%';
GRANT INSERT ON test_drive_dev.* to 'test_drive_dev_user'@'%';
GRANT DELETE ON test_drive_dev.* to 'test_drive_dev_user'@'%';
GRANT UPDATE ON test_drive_dev.* to 'test_drive_dev_user'@'%';
GRANT SELECT ON test_drive_prod.* to 'test_drive_prod_user'@'%';
GRANT INSERT ON test_drive_prod.* to 'test_drive_prod_user'@'%';
GRANT DELETE ON test_drive_prod.* to 'test_drive_prod_user'@'%';
GRANT UPDATE ON test_drive_prod.* to 'test_drive_prod_user'@'%';