



CREATE TABLE USERS(
	ID int auto_increment primary key,
    Username varchar(100),
    Password varchar(100),
    Publickey varchar(10000))
DELIMITER //
CREATE PROCEDURE CreateUser(Username varchar(100), Password varchar(100)) 
BEGIN
	INSERT INTO users(Username,Password)
    VALUES (Username,Password);
END
//
DELIMITER ;
DELIMITER //
	CREATE FUNCTION CheckRegister(Usernamess varchar(100))
    RETURNS BOOLEAN
    DETERMINISTIC
    BEGIN
    DECLARE Usernames varchar(100) default ("");
    SELECT Username
    INTO Usernames
    FROM Users
    WHERE Username = Usernamess;
    IF(Usernames = "") THEN return true;
    ELSE return false;
    END IF;
    END
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE Login(Usernames varchar(100), Passwords varchar(100), PublicKey varchar(1000)) 
BEGIN
	SELECT *
    FROM Users
    WHERE Username = Usernames AND Password = Passwords;
    update users
    SET Publickey = PublicKey
    WHERE Username = Usernames AND Password = Passwords;
END
//
DELIMITER ;

SET SQL_SAFE_UPDATES = 0;
DELIMITER //
CREATE PROCEDURE GetUser() 
BEGIN
	SELECT Username
    FROM Users;
END
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE GetKeyUser(Usernames varchar(100)) 
BEGIN
	SELECT Username,PublicKey
    FROM Users
    WHERE Username = Usernames;
END
//
DELIMITER ;
