package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetUserById(uuid uuid.UUID) models.User {
	panic("not implemented")
}

func GetUserByLogin(login string) (models.User, error) {
	var user models.User
	logger.InfoLog.Print(login)
	query, err := db.Query("SELECT * FROM users WHERE login = $1", login)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get user by login")
		return user, err
	}
	if !query.Next() {
		logger.ErrorLog.Print("No data was extracted from query")
	}
	err = query.Scan(&user.Uuid, &user.Password, &user.Login, &user.Surname, &user.Name, &user.LastName)
	if err != nil {
		logger.ErrorLog.Print("Model did not match the one in database")
	}
	return user, err
}
