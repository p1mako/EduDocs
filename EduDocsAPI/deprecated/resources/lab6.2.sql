CREATE PROCEDURE AddUser
    @password NVARCHAR(128),
    @login NVARCHAR(128),
    @lastName NVARCHAR(128),
    @name NVARCHAR(128),
    @surname NVARCHAR(128)
AS
INSERT INTO Users
    ([password], [login], [lastName], [name], [surname])
VALUES
    (
        @password,
        @login,
        @lastName,
        @name,
        @surname
);
GO

CREATE PROCEDURE AddAdmin
    @password NVARCHAR(128),
    @login NVARCHAR(128),
    @lastName NVARCHAR(128),
    @name NVARCHAR(128),
    @surname NVARCHAR(128),
    @assignment_start NVARCHAR(50),
    @assignment_end NVARCHAR(50),
    @role TINYINT
AS
BEGIN
    EXEC AddUser @password, @login, @lastName, @name, @surname;
    INSERT INTO Admins
        (id, assignment_start, assignment_end, [role])
    VALUES
        (
            (SELECT id
            FROM Users
            WHERE [login] = @login),
            CAST(@assignment_start as datetime2),
            CAST(@assignment_end as datetime2),
            @role
);
END
GO

CREATE PROCEDURE AddProfessor
    @password NVARCHAR(128),
    @login NVARCHAR(128),
    @lastName NVARCHAR(128),
    @name NVARCHAR(128) ,
    @surname NVARCHAR(128) ,
    @degree NVARCHAR(128) 
AS
BEGIN
    EXEC AddUser @password, @login, @lastName, @name, @surname;
    INSERT INTO Professors
        (id, degree)
    VALUES
        (
            (SELECT id
            FROM Users
            WHERE [login] = @login),
            @degree
        );
END
GO
CREATE PROCEDURE AddSpecialization
    @name NVARCHAR(128) ,
    @registerNum NVARCHAR(30) 
AS
INSERT INTO Specializations
    ([name], registerNumber)
VALUES
    (
        @name,
        @registerNum  
);
GO
CREATE PROCEDURE AddStudent
    @password NVARCHAR(128) ,
    @login NVARCHAR(128) ,
    @lastName NVARCHAR(128),
    @name NVARCHAR(128) ,
    @surname NVARCHAR(128) ,
    @groupNum TINYINT,
    @status TINYINT ,
    @entryDate NVARCHAR(128) ,
    @uniqueNum NVARCHAR(20) ,
    @specialization NVARCHAR(128) 
AS
BEGIN
    EXEC AddUser @password, @login, @lastName, @name, @surname;
    INSERT INTO Students
    VALUES
        (
            (SELECT id
            FROM Users
            WHERE [login] = @login),
            @groupNum,
            @status,
            @entryDate,
            @uniqueNum,
            @specialization
);
END
GO
CREATE PROCEDURE AddTemplate
    @routeToDocumnet NVARCHAR(128),
    @name NVARCHAR(128)
AS
INSERT INTO Templates
    (route_to_document, [name])
VALUES
    (
        @routeToDocumnet,
        @name
);
GO
CREATE PROCEDURE AddAdministrationDocumnet
    @admin VARCHAR(50) ,
    @template VARCHAR(150) 
AS
INSERT INTO AdministrationDocuments
    (administration_member, template)
VALUES
    (
        (SELECT id
        FROM Users
        WHERE [login] = @admin),
        (SELECT id
        FROM Templates
        WHERE [name] = @template)
);
GO
CREATE PROCEDURE AddDocument
    @template NVARCHAR(50),
    @validThrough NVARCHAR(50),
    @author NVARCHAR(50),
    @initiator NVARCHAR(50),
    @created NVARCHAR(50)
AS
INSERT INTO Documents
    (template, valid_Through, author, initiator, created)
VALUES
    (
        (SELECT id
        FROM Templates
        WHERE @template = name),
        CAST(@validThrough as datetime2),
        (SELECT id
        FROM Users
        WHERE login = @author),
        (SELECT id
        FROM Users
        WHERE login = @initiator),
        CAST(@created as datetime2)
    );
GO
CREATE PROCEDURE AddRequest
    @created NVARCHAR(120),
    @status TINYINT ,
    @document UNIQUEIDENTIFIER,
    @initiator NVARCHAR(50) ,
    @template NVARCHAR(50)
AS
INSERT INTO Requests
    ([created], [status], document, initiator, template)
VALUES
    (
        IIF(@created = NULL, SYSDATETIME(), CAST(@created as datetime2)),
        @status,
        @document,
        (SELECT id
        FROM Users
        WHERE [login] = @initiator),
        (SELECT id
        FROM Templates
        WHERE [name] = @template)
);
GO

CREATE PROCEDURE AddMessage
    @text NVARCHAR(128) ,
    @request UNIQUEIDENTIFIER 
AS
INSERT INTO Messages
    ([text], request)
VALUES
    (
        @text,
        @request
);
GO



INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('9iuo8nvw54n98e5', 'fpm.Orlovich', 'Orlovich', 'Yuri', 'Leonidovich')
INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('o98,pluj68h7egv', 'fpm.Hartov', 'Hartov', 'Stanislave', 'Vitalievich')
INSERT INTO Users
    (password, login, name, surname)
VALUES('-p0;p09ol9oi8kiu8j7u7yh8', 'fpm.Hubko', 'Anatoliy', 'Hubko')
INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('o98,pluj68h7egv', 'fpm.Koshchanka', 'Koshchanka', 'Vladislav', 'Dmitrievich')
INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('-p0;p09ol9oi8kiu8j7u7yh8', 'fpm.Mayorov', 'Mayorov', 'Dmitry', 'Ivanovich')
INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('o98,pluj68h7egv', 'fpm.Leschik', 'Leschik', 'Dmitry', 'Anatolievich')
INSERT INTO Users
    (password, login, name, surname)
VALUES('-p0;p09ol9oi8kiu8j7u7yh8', 'fpm.Panulin', 'Panulin', 'Anatoliy')
INSERT INTO Users
    (password, login, name, surname)
VALUES('-p0;p09ol9oi8kiu8j7u7yh8', 'fpm.Burak', 'Burak', 'Victoria')
INSERT INTO Users
    (password, login, name, surname)
VALUES('o98,pluj68h7egv', 'fpm.Kozlov', 'Kozlov', 'Yuriy')
INSERT INTO Users
    (password, login, surname, name, lastName)
VALUES('o98,pluj68h7egv', 'fpm.Nichiporuk', 'Nichiporuk', 'Roman', 'Olegovich')

INSERT INTO Admins
    (id, assignment_start, assignment_end, role)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Orlovich'), CAST('2022-11-4 10:00:00' AS smalldatetime), NULL, 1)

INSERT INTO Professors
    (id, degree)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Orlovich'), 'Docent')

INSERT INTO Specializations
    (name, registerNumber)
VALUES('Informatics', '1-31 03 04')
INSERT INTO Specializations
    (name, registerNumber)
VALUES('Cyber security', '1-31 01 01')
INSERT INTO Specializations
    (name, registerNumber)
VALUES('Applied informatics', '1-31 03 07')

INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Leschik'), 1, 0, CAST('2021-08-01' AS datetime2), '2123201', (SELECT id
        FROM Specializations
        WHERE name = 'Informatics'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Hartov'), 1, 0, CAST('2021-08-01' AS datetime2), '2123202', (SELECT id
        FROM Specializations
        WHERE name = 'Informatics'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Hubko'), 1, 0, CAST('2021-08-01' AS datetime2), '2123203', (SELECT id
        FROM Specializations
        WHERE name = 'Informatics'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Koshchanka'), 1, 0, CAST('2020-08-01' AS datetime2), '2123204', (SELECT id
        FROM Specializations
        WHERE name = 'Cyber security'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Mayorov'), 1, 0, CAST('2021-08-01' AS datetime2), '2123205', (SELECT id
        FROM Specializations
        WHERE name = 'Cyber security'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Panulin'), 1, 0, CAST('2021-08-01' AS datetime2), '2123206', (SELECT id
        FROM Specializations
        WHERE name = 'Cyber security'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Burak'), 1, 0, CAST('2021-08-01' AS datetime2), '2123207', (SELECT id
        FROM Specializations
        WHERE name = 'Applied informatics'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Kozlov'), 1, 0, CAST('2021-08-01' AS datetime2), '2123208', (SELECT id
        FROM Specializations
        WHERE name = 'Applied informatics'))
INSERT INTO Students
    (id, group_num, status, entry_date, uniqueNumber, specialization)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Nichiporuk'), 1, 0, CAST('2021-08-01' AS datetime2), '2123209', (SELECT id
        FROM Specializations
        WHERE name = 'Applied informatics'))

INSERT INTO Templates
    (route_to_document, name)
VALUES('/home/doc1.pdf', 'Confirmation of education')
INSERT INTO Templates
    (route_to_document, name)
VALUES('/home/doc2.pdf', 'Acceptance for work')
INSERT INTO Templates
    (route_to_document, name)
VALUES('/home/doc3.pdf', 'Characteristics')

INSERT INTO AdministrationDocuments
    (administration_member, template)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Orlovich'), (SELECT id
        FROM Templates
        WHERE [name] = 'Confirmation of education'))
INSERT INTO AdministrationDocuments
    (administration_member, template)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Orlovich'), (SELECT id
        FROM Templates
        WHERE [name] = 'Acceptance for work'))
INSERT INTO AdministrationDocuments
    (administration_member, template)
VALUES((SELECT id
        FROM Users
        WHERE [login] = 'fpm.Orlovich'), (SELECT id
        FROM Templates
        WHERE [name] = 'Characteristics'));

EXEC AddDocument 'Acceptance for work', '2023-04-15 00:00:00', 'fpm.Orlovich', 'fpm.Leschik', '2024-04-15 15:00:00';
EXEC AddDocument 'Confirmation of education', '2022-04-15 00:00:00', 'fpm.Orlovich', 'fpm.Leschik', '2023-04-15 15:00:00';
EXEC AddDocument 'Acceptance for work', '2023-04-14 00:00:00', 'fpm.Orlovich', 'fpm.Hartov', '2024-04-14 15:00:00';
EXEC AddDocument 'Characteristics', '2023-04-13 00:00:00', 'fpm.Orlovich', 'fpm.Hubko', '2024-04-13 15:00:00';
EXEC AddDocument 'Acceptance for work', '2022-04-13 00:00:00', 'fpm.Orlovich', 'fpm.Hubko', '2023-04-13 15:00:00';
EXEC AddDocument 'Acceptance for work', '2023-04-12 00:00:00', 'fpm.Orlovich', 'fpm.Koshchanka', '2024-04-12 15:00:00';
EXEC AddDocument 'Confirmation of education', '2022-04-12 00:00:00', 'fpm.Orlovich', 'fpm.Koshchanka', '2023-04-12 15:00:00';
EXEC AddDocument 'Acceptance for work', '2021-04-12 00:00:00', 'fpm.Orlovich', 'fpm.Koshchanka', '2022-04-12 15:00:00';
EXEC AddDocument 'Characteristics', '2023-04-11 00:00:00', 'fpm.Orlovich', 'fpm.Burak', '2024-04-11 15:00:00';
EXEC AddDocument 'Acceptance for work', '2023-04-10 00:00:00', 'fpm.Orlovich', 'fpm.Mayorov', '2024-04-10 15:00:00';
EXEC AddDocument 'Acceptance for work', '2022-04-10 00:00:00', 'fpm.Orlovich', 'fpm.Mayorov', '2023-04-10 15:00:00';
EXEC AddDocument 'Confirmation of education', '2023-04-9 00:00:00', 'fpm.Orlovich', 'fpm.Kozlov', '2024-04-9 15:00:00';
EXEC AddDocument 'Characteristics', '2023-04-8 00:00:00', 'fpm.Orlovich', 'fpm.Nichiporuk', '2024-04-8 15:00:00';

DECLARE @document UNIQUEIDENTIFIER;

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-15 00:00:00' as datetime2));
EXEC AddRequest '2023-04-15 14:30:00', 4, @document, 'fpm.Leschik', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2022-04-15 00:00:00' as datetime2));
EXEC AddRequest '2022-04-15 14:30:00', 4, @document, 'fpm.Leschik', 'Confirmation of education';

SET @document = NULL;
EXEC AddRequest '2021-04-15 14:30:00', 5, @document, 'fpm.Leschik', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-14 00:00:00' as datetime2));
EXEC AddRequest '2023-04-14 14:30:00', 4, @document, 'fpm.Hartov', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2022-04-14 14:30:00', 5, @document, 'fpm.Hartov', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-14 14:30:00', 5, @document, 'fpm.Hartov', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-13 00:00:00' as datetime2));
EXEC AddRequest '2023-04-13 14:30:00', 4, @document, 'fpm.Hubko', 'Characteristics';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2022-04-13 00:00:00' as datetime2));
EXEC AddRequest '2022-04-13 14:30:00', 4, @document, 'fpm.Hubko', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-13 14:30:00', 5, @document, 'fpm.Hubko', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-12 00:00:00' as datetime2));
EXEC AddRequest '2023-04-12 14:30:00', 4, @document, 'fpm.Koshchanka', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2022-04-12 00:00:00' as datetime2));
EXEC AddRequest '2022-04-12 14:30:00', 4, @document, 'fpm.Koshchanka', 'Confirmation of education';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2021-04-12 00:00:00' as datetime2));
EXEC AddRequest '2021-04-12 14:30:00', 4, @document, 'fpm.Koshchanka', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-11 00:00:00' as datetime2));
EXEC AddRequest '2023-04-11 14:30:00', 4, @document, 'fpm.Mayorov', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2022-04-11 00:00:00' as datetime2));
EXEC AddRequest '2022-04-11 14:30:00', 4, @document, 'fpm.Mayorov', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-11 14:30:00', 5, @document, 'fpm.Mayorov', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-10 00:00:00' as datetime2));
EXEC AddRequest '2023-04-10 14:30:00', 4, @document, 'fpm.Burak', 'Characteristics';

SET @document = NULL;
EXEC AddRequest '2022-04-10 14:30:00', 5, @document, 'fpm.Burak', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-10 14:30:00', 5, @document, 'fpm.Burak', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-9 00:00:00' as datetime2));
EXEC AddRequest '2023-04-9 14:30:00', 4, @document, 'fpm.Kozlov', 'Confirmation of education';

SET @document = NULL;
EXEC AddRequest '2022-04-9 14:30:00', 5, @document, 'fpm.Kozlov', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-9 14:30:00', 5, @document, 'fpm.Kozlov', 'Acceptance for work';

SET @document = (SELECT id FROM Documents WHERE created = CAST('2023-04-8 00:00:00' as datetime2));
EXEC AddRequest '2023-04-8 14:30:00', 4, @document, 'fpm.Nichiporuk', 'Characteristics';

SET @document = NULL;
EXEC AddRequest '2022-04-8 14:30:00', 5, @document, 'fpm.Nichiporuk', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2021-04-8 14:30:00', 5, @document, 'fpm.Nichiporuk', 'Acceptance for work';

SET @document = NULL;
EXEC AddRequest '2023-04-7 14:30:00', 2, @document, 'fpm.Panulin', 'Acceptance for work';

EXEC AddRequest '2022-04-7 14:30:00', 5, @document, 'fpm.Panulin', 'Acceptance for work';

EXEC AddRequest '2021-04-7 14:30:00', 5, @document, 'fpm.Panulin', 'Acceptance for work';

INSERT INTO Messages
    (text, request)
VALUES
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-15 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-15 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-15 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-15 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-15 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-15 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-15 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-15 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-15 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-14 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-14 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-14 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-14 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-14 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-14 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-14 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-14 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-14 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-13 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-13 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-13 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-13 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-13 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-13 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-13 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-13 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-13 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-12 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-12 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-12 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-12 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-12 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-12 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-12 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-12 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-12 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-11 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-11 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-11 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-11 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-11 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-11 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-11 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-11 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-11 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-10 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-10 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-10 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-10 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-10 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-10 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-10 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-10 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-10 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-9 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-9 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-9 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-9 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-9 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-9 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-9 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-9 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-9 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-8 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-8 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-8 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-8 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-8 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-8 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-8 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-8 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-8 14:30:00' AS datetime2))),
    ('Add new data please', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-7 14:30:00' AS datetime2))),
    ('Confirmed', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-7 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2023-04-7 14:30:00' AS datetime2))),
    ('Can you do it today, please?', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-7 14:30:00' AS datetime2))),
    ('Got it', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-7 14:30:00' AS datetime2))),
    ('Thanks', (SELECT id
        FROM Requests
        WHERE created = CAST('2022-04-7 14:30:00' AS datetime2))),
    ('Purpose: to show a moms work', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-7 14:30:00' AS datetime2))),
    ('Specialization', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-7 14:30:00' AS datetime2))),
    ('Thanks a lot', (SELECT id
        FROM Requests
        WHERE created = CAST('2021-04-7 14:30:00' AS datetime2)));

--1,4 find all pairs of students with the same specialty
WITH
    StudentPairs
    AS
    (
        SELECT s1.id AS first_student, s2.id AS second_student, s1.specialization
        FROM Students s1 JOIN Students s2 ON s1.specialization != s2.specialization
    )
SELECT users1.surname AS student_1, users2.surname AS student_2, Specializations.name AS specialization_name
FROM Specializations JOIN StudentPairs sp ON specialization = Specializations.id JOIN Users users1 ON users1.id = first_student JOIN Users users2 ON users2.id = second_student
ORDER BY specialization_name
--2 decrypt all the statuses in requests
SELECT Users.surname, Requests.[created],
    CASE Requests.[status] 
            WHEN 1 THEN 'Sent'
            WHEN 2 THEN 'Being processed'
            WHEN 3 THEN 'Can be taken'
            WHEN 4 THEN 'Received'
            WHEN 5 THEN 'Declined'
            WHEN 6 THEN 'Removed'
        END AS [Status]
FROM Requests JOIN Users ON Users.id = initiator
--3,5 determine if student is currently studying
GO
CREATE VIEW To_Do (Surname, time_created, to_do) AS
SELECT Users.surname, Requests.[created], 
        CASE
            WHEN [status] = 1 AND YEAR(created) < 2021 THEN 'Must read'
            WHEN [status] = 1 AND YEAR(created) < 2022 THEN 'Should read'
            WHEN [status] = 1 AND YEAR(created) < 2023 THEN 'to read'
            WHEN ([status] = 4 OR [status] = 5) AND YEAR(created) < 2022 THEN 'Must delete'
            WHEN ([status] = 4 OR [status] = 5) AND YEAR(created) < 2023 THEN 'Should delete'
            WHEN ([status] = 4 OR [status] = 5) AND YEAR(created) < 2024 THEN 'to delete'
            ELSE 'Ok'
        END AS [To_do]
FROM Requests JOIN Users ON Users.id = initiator
GO
-- 6 find all most needed tasks by status
SELECT created, [status], document, Users.surname, Templates.name
FROM Requests JOIN Users ON initiator = Users.id JOIN Templates ON template = Templates.id
WHERE status = (SELECT MIN([status])
FROM Requests)
-- 7 in creation(For example Procedure AddStudent)

-- 8 Null all documents made in year 2023
SELECT Documents.id, Users.surname, NULLIF(YEAR(created), 2023) AS year_created
FROM Documents JOIN Users on author = Users.id

-- 9 get top 9 of documents created by date\
SELECT TOP 9
    Documents.id, Users.surname, Documents.created AS author
FROM Documents JOIN Users ON Users.id = Documents.author
ORDER BY created DESC
-- 10 get all specialties students
SELECT Specializations.name, Users.surname
FROM Students JOIN Users ON Users.id = Students.id JOIN Specializations ON specialization = Specializations.id
GROUP BY Specializations.name, Users.surname WITH ROLLUP

-- 11 Sync Professors and Users
MERGE INTO Professors USING Users ON (Users.id = Professors.id)
WHEN NOT MATCHED BY TARGET THEN INSERT(id) VALUES(Users.id);
SELECT degree, [password], [login], lastName, name, surname FROM Professors INNER JOIN Users ON Users.id = Professors.id