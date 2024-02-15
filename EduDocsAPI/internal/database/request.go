package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
)

func GetAllRequests(username string) ([]models.Request, error) {
	var requests []models.Request
	query, err := db.Query("SELECT * FROM requests")
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get requests")
		return requests, err
	}
	if !query.Next() {
		logger.ErrorLog.Print("No data was extracted from query")
		return requests, err
	}
	for query.Next() {
		var request models.Request
		err = query.Scan(request.Uuid, request.Created, request.Status, request.Document.Uuid, request.Initiator.Uuid, request.Template.Uuid)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return requests, err
		}
		requests = append(requests, request)
	}
	return requests, err
}
