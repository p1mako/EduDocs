package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetUserById(id uuid.UUID) (models.User, error) {
	var user models.User
	query, err := db.Query("SELECT * FROM users WHERE id = $1", id)
	if err != nil {
		logger.ErrorLog.Print("Error while extracting user: ", err)
		return models.User{}, err
	}
	if !query.Next() {
		logger.InfoLog.Printf("No data of user was extracted for %s", id)
	}
	err = query.Scan(user.Uuid, user.Password, user.Login, user.LastName, user.Name, user.Surname)
	if err != nil {
		logger.ErrorLog.Print("Could not read input from query: ", err)
		return models.User{}, err
	}
	return user, nil
}

func GetUserByLogin(login string) (models.User, error) {
	var user models.User
	query, err := db.Query("SELECT * FROM users WHERE login = $1", login)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get user by login")
		return user, err
	}
	if !query.Next() {
		logger.ErrorLog.Print("No data was extracted from query")
		return user, err
	}
	err = query.Scan(&user.Uuid, &user.Password, &user.Login, &user.Surname, &user.Name, &user.LastName)
	if err != nil {
		logger.ErrorLog.Print("Model did not match the one in database")
	}
	return user, err
}
