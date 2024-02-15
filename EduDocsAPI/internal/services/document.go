package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetDocumentById(id uuid.UUID) (models.Document, error) {
	document, err := database.GetDocumentById(id)
	if err != nil {
		logger.InfoLog.Printf("Could not extract data for document with id: %s and error: %s", id, err.Error())
		return models.Document{}, err
	}
	document.Initiator, err = GetUserById(document.Initiator.Uuid)
	if err != nil {
		return models.Document{}, err
	}
	document.Template, err = GetTemplateById(document.Template.Uuid)
	if err != nil {
		return models.Document{}, err
	}
	return document, nil
}
