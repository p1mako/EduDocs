package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetUserById(id uuid.UUID) (*models.User, error) {
	user, err := database.GetUserById(id)
	if err != nil {
		logger.ErrorLog.Print("Could not extract user by id from db")
	}
	return user, err
}

func GetUserByLogin(login string) (*models.User, error) {
	user, err := database.GetUserByLogin(login)
	if err != nil {
		logger.ErrorLog.Print("Could not extract user by login from db")
	}
	return user, err
}

func GetAdmin(user *models.User) (*models.Admin, error) {
	admin, err := database.GetAdmin(user)
	if err != nil {
		logger.ErrorLog.Print("Cannot read admin from db")
	}
	return admin, err
}

func GetProfessor(user *models.User) (*models.Professor, error) {
	professor, err := database.GetProfessor(user)
	if err != nil {
		logger.ErrorLog.Print("Cannot read admin from db")
	}
	return professor, err
}

func GetStudent(user *models.User) (*models.Student, error) {
	student, err := database.GetStudent(user)
	if err != nil {
		logger.ErrorLog.Print("Cannot read admin from db")
	}
	return student, err
}

func GetExtendedUser(user *models.User) (*models.Student, *models.Professor, *models.Admin, error) {
	student, err := GetStudent(user)
	professor, err := GetProfessor(user)
	admin, err := GetAdmin(user)
	return student, professor, admin, err
}
