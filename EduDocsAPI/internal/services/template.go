package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/models"
	"errors"
	"github.com/google/uuid"
)

func GetTemplateById(id uuid.UUID) (models.Template, error) {
	return database.GetTemplateById(id)
}

func GetAllTemplates() ([]models.Template, error) {
	return database.GetAllTemplates()
}

func AddTemplate(template models.Template) error {
	if template.RouteToDocument == "" || template.Name == "" {
		return errors.New("empty input")
	}
	return database.AddTemplate(template)
}
