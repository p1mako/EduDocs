package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/models"
	"errors"
)

func GetTemplateById() ([]models.Template, error) {
	panic("Not implemented")
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
