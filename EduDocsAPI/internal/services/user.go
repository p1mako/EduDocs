package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetUserById(id uuid.UUID) (*models.User, error) {
	return database.GetUserById(id)
}

func GetUserByLogin(login string) (*models.User, error) {
	return database.GetUserByLogin(login)
}
