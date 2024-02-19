package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetDocumentById(id uuid.UUID) (*models.Document, error) {
	var document *models.Document
	query, err := db.Query("SELECT * FROM users WHERE id = $1", id)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Error while extracting document: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	err = query.Scan(&document.Uuid, &document.Template.Uuid, &document.ValidThrough, &document.Author.Uuid, &document.Initiator.Uuid, &document.Created)
	if err != nil {
		logger.ErrorLog.Print("Could not read input from query: ", err)
		return nil, err
	}
	return document, nil
}
