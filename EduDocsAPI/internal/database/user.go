package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetUserById(id uuid.UUID) (*models.User, error) {
	var user *models.User
	query, err := db.Query("SELECT * FROM users WHERE id = $1", id)
	if err != nil {
		logger.ErrorLog.Print("Error while extracting user: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	err = query.Scan(&user.Uuid, &user.Password, &user.Login, &user.LastName, &user.Name, &user.Surname)
	if err != nil {
		logger.ErrorLog.Print("Could not read input from query: ", err)
		return nil, err
	}
	return user, nil
}

func GetUserByLogin(login string) (*models.User, error) {
	user := new(models.User)
	query, err := db.Query("SELECT * FROM users WHERE login = $1", login)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get user by login")
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	err = query.Scan(&user.Uuid, &user.Password, &user.Login, &user.Surname, &user.Name, &user.LastName)
	if err != nil {
		logger.ErrorLog.Print("Model user did not match the one in database")
		return nil, err
	}
	return user, nil
}

func GetAdmin(user *models.User) (*models.Admin, error) {
	query, err := db.Query("SELECT a.assignment_start, a.assignment_end, a.role FROM users JOIN public.admins a on users.id = a.id WHERE a.id = $1", user.Uuid)
	if err == nil {
		logger.ErrorLog.Print("Cannot read from db: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	admin := new(models.Admin)
	err = query.Scan(&admin.From, &admin.Until, &admin.Role)
	if err != nil {
		logger.ErrorLog.Print("Cannot scan extracted model: ", err)
		return nil, err
	}
	admin.User = *user
	return admin, nil
}

func GetProfessor(user *models.User) (*models.Professor, error) {
	query, err := db.Query("SELECT p.degree FROM users JOIN public.professors p on users.id = p.id WHERE p.id = $1", user.Uuid)
	if err == nil {
		logger.ErrorLog.Print("Cannot read from db: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	professor := new(models.Professor)
	err = query.Scan(&professor.Degree)
	if err != nil {
		logger.ErrorLog.Print("Cannot scan extracted model: ", err)
		return nil, err
	}
	professor.User = *user
	return professor, nil
}

func GetStudent(user *models.User) (*models.Student, error) {
	query, err := db.Query("SELECT s.entry_date, s.group_num, s.specialization, s.status, s.uniquenumber FROM users JOIN public.students s on users.id = s.id WHERE s.id = $1", user.Uuid)
	if err == nil {
		logger.ErrorLog.Print("Cannot read from db: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	student := new(models.Student)
	err = query.Scan(&student.EntryDate, &student.Group, &student.Specialization, &student.Status, &student.UniqueNumber)
	if err != nil {
		logger.ErrorLog.Print("Cannot scan extracted model: ", err)
		return nil, err
	}
	student.User = *user
	return student, nil
}
