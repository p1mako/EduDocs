package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetAllRequests(username string) ([]models.Request, error) {
	requests, err := database.GetAllRequests(username)
	if err != nil {
		return nil, err
	}
	for _, request := range requests {
		request.Template, err = GetTemplateById(request.Template.Uuid)
		if err != nil {
			logger.InfoLog.Print("Could not get template for request")
			return nil, err
		}
		request.Initiator, err = GetUserById(request.Initiator.Uuid)
		if err != nil {
			logger.InfoLog.Print("Could not get template for request")
			return nil, err
		}
		if request.Document.Uuid != uuid.Nil {
			request.Document, err = GetDocumentById(request.Document.Uuid)
			if err != nil {
				logger.InfoLog.Print("Could not get document for request")
				return nil, err
			}
			return nil, err
		}
	}
	return requests, nil
}
