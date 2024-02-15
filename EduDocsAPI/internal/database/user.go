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
	var user *models.User
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
