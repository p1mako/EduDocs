CREATE DATABASE edudocs
GO
USE edudocs

CREATE LOGIN Dev WITH PASSWORD = 'edSDD2002';
CREATE USER Dev for LOGIN Dev;
GRANT CONTROL To Dev;

CREATE TABLE Users(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                    password NVARCHAR(128) NOT NULL,
                    login NVARCHAR(128) NOT NULL UNIQUE,
                    lastName NVARCHAR(128),
                    name NVARCHAR(128) NOT NULL,
                    surname NVARCHAR(128) NOT NULL);
CREATE TABLE Admins(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                    assignment_start DATETIME2 NOT NULL,
                    assignment_end DATETIME2,
                    role TINYINT NOT NULL,
                    FOREIGN KEY (id) REFERENCES Users (id));
CREATE TABLE Professors(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        degree NVARCHAR(128) NOT NULL,
                       FOREIGN KEY (id) REFERENCES Users (id));
CREATE TABLE Specializations(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                             name NVARCHAR(128) NOT NULL ,
                             registerNumber NVARCHAR(30) NOT NULL);
CREATE TABLE Students(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        group_num TINYINT,
                        status TINYINT NOT NULL,
                        entry_date DATETIME2 NOT NULL,
                        uniqueNumber NVARCHAR(20) NOT NULL UNIQUE,
                        specialization UNIQUEIDENTIFIER,
                        FOREIGN KEY (id) REFERENCES Users(id),
                        FOREIGN KEY (specialization) REFERENCES Specializations(id));


CREATE TABLE Templates(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                        route_to_document NVARCHAR(128),
                        name NVARCHAR(128) NOT NULL);
CREATE TABLE AdministrationDocuments(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                                    administration_member UNIQUEIDENTIFIER,
                                    template UNIQUEIDENTIFIER,
                                    FOREIGN KEY (administration_member) REFERENCES Admins(id),
                                    FOREIGN KEY (template) REFERENCES Templates(id));
CREATE TABLE Documents(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                        template UNIQUEIDENTIFIER NOT NULL,
                        valid_Through DATE,
                        author UNIQUEIDENTIFIER NOT NULL,
                        initiator UNIQUEIDENTIFIER NOT NULL,
                        created DATETIME2 NOT NULL,
                        FOREIGN KEY (template) REFERENCES Templates(id),
                        FOREIGN KEY (author) REFERENCES Admins(id),
                        FOREIGN KEY (initiator) REFERENCES Users(id));
CREATE TABLE Requests(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                        created DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
                        status TINYINT NOT NULL,
                        document UNIQUEIDENTIFIER,
                        FOREIGN KEY (document) REFERENCES Documents(id));
CREATE TABLE Messages(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                      text NVARCHAR(128) NOT NULL ,
                      time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
                      request UNIQUEIDENTIFIER NOT NULL ,
                      FOREIGN KEY (request) REFERENCES Requests(id));