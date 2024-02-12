package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/models"
)

func GetTemplateById() ([]models.Template, error) {
	panic("Not implemented")
}

func GetAllTemplates() ([]models.Template, error) {
	return database.GetAllTemplates()
}
