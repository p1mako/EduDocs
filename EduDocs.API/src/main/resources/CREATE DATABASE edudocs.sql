CREATE DATABASE edudocs
GO
USE edudocs
CREATE TABLE Users(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                    password NVARCHAR(128) NOT NULL,
                    login NVARCHAR(128) NOT NULL UNIQUE,
                    lastName NVARCHAR(128),
                    name NVARCHAR(128) NOT NULL,
                    surname NVARCHAR(128) NOT NULL);
CREATE TABLE Admins(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                    assignment_start DATETIME2 NOT NULL,
                    assignment_end DATETIME2,
                    role TINYINT,
                    FOREIGN KEY (id) REFERENCES Users (id));
CREATE TABLE Professors(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        degree NVARCHAR(128),
                       FOREIGN KEY (id) REFERENCES Users (id));
CREATE TABLE Specializations(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                             name NVARCHAR(128),
                             registerNumber NVARCHAR(30));
CREATE TABLE Students(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        group_num TINYINT,
                        status TINYINT NOT NULL,
                        entry_date TIMESTAMP NOT NULL,
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
                        created TIMESTAMP NOT NULL,
                        FOREIGN KEY (template) REFERENCES Templates(id),
                        FOREIGN KEY (author) REFERENCES Admins(id),
                        FOREIGN KEY (initiator) REFERENCES Users(id));
CREATE TABLE Requests(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                        created TIMESTAMP,
                        status TINYINT,
                        document UNIQUEIDENTIFIER,
                        FOREIGN KEY (document) REFERENCES Documents(id));
CREATE TABLE Messages(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                      text NVARCHAR(128) NOT NULL ,
                      time TIMESTAMP NOT NULL DEFAULT (SYSDATETIME()),
                      request UNIQUEIDENTIFIER NOT NULL ,
                      FOREIGN KEY (request) REFERENCES Requests(id));
CREATE TABLE Disciplines(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                            nam NVARCHAR(128) NOT NULL ,
                            year CHAR(4) NOT NULL ,
                            lecturer UNIQUEIDENTIFIER NOT NULL ,
                            specialization UNIQUEIDENTIFIER NOT NULL,
                            FOREIGN KEY (lecturer) REFERENCES Professors(id),
                            FOREIGN KEY (specialization) REFERENCES Specializations(id))
CREATE TABLE Locations(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                        address NVARCHAR(128) NOT NULL,
                        auditorium NVARCHAR(4) NOT NULL);

CREATE TABLE TimetableElements(id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT(NEWID()),
                                day_of_week TINYINT NOT NULL ,
                                location UNIQUEIDENTIFIER NOT NULL ,
                                lesson_type TINYINT NOT NULL,
                                professor UNIQUEIDENTIFIER NOT NULL,
                                lesson_num CHAR(1),
                                discipline UNIQUEIDENTIFIER,
                                start_time TIME NOT NULL ,
                                end_time TIME NOT NULL ,
                                FOREIGN KEY (location) REFERENCES Locations(id),
                                FOREIGN KEY (professor) REFERENCES  Professors(id),
                                FOREIGN KEY (discipline) REFERENCES Disciplines(id));