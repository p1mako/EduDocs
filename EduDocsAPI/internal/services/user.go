package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/models"
)

func GetUserByLogin(login string) (models.User, error) {
	return database.GetUserByLogin(login)
}
