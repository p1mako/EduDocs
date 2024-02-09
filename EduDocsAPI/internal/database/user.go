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
	query, err := db.Query("SELECT * FROM users WHERE login = ?", login)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get user by login")
		return user, err
	}
	err = query.Scan(&user)
	logger.ErrorLog.Print("Model did not match the one in database")
	return user, err
}
