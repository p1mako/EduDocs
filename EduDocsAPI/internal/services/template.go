package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"errors"
	"github.com/google/uuid"
)

func GetTemplateById(id uuid.UUID) (*models.Template, error) {
	template, err := database.GetTemplateById(id)
	if err != nil {
		logger.ErrorLog.Print("Could not extract template by uuid from db")
	}
	return template, err
}

func GetAllTemplates() ([]*models.Template, error) {
	templates, err := database.GetAllTemplates()
	if err != nil {
		logger.ErrorLog.Print("Could not extract all templates from db")
	}
	return templates, err
}

func AddTemplate(template models.Template) error {
	if template.RouteToDocument == "" || template.Name == "" {
		return errors.New("empty input")
	}
	err := database.AddTemplate(template)
	if err != nil {
		logger.ErrorLog.Print("Could not add template to database")
	}
	return err
}
