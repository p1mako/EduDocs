package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
)

func GetAllAvailableRequestsForAdmin(user *models.Admin) ([]*models.Request, error) {
	var requests []*models.Request
	//TODO:extract data from template or duplicate to request
	query, err := db.Query("SELECT * FROM requests WHERE template")
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get requests")
		return requests, err
	}
	if !query.Next() {
		return requests, nil
	}
	for query.Next() {
		var request *models.Request
		err = query.Scan(&request.Uuid, &request.Created, &request.Status, &request.Document.Uuid, &request.Initiator.Uuid, &request.Template.Uuid)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return nil, err
		}
		requests = append(requests, request)
	}
	return requests, err
}

func GetAllAvailableRequestsForUser(user models.User) ([]*models.Request, error) {
	var requests []*models.Request
	query, err := db.Query("SELECT * FROM requests WHERE initiator = $1", user.Uuid)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get requests")
		return requests, err
	}
	if !query.Next() {
		return requests, nil
	}
	for query.Next() {
		var request *models.Request
		err = query.Scan(&request.Uuid, &request.Created, &request.Status, &request.Document.Uuid, &request.Initiator.Uuid, &request.Template.Uuid)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return nil, err
		}
		requests = append(requests, request)
	}
	return requests, err
}
