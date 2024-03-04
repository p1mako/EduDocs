package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"fmt"
)

func GetAllAvailableRequestsForAdmin(user *models.Admin) ([]*models.Request, error) {
	requests := []*models.Request{}
	query, err := db.Query("SELECT requests.id, created, status, document, initiator, template FROM requests JOIN templates as t on requests.template = t.id WHERE t.responsible_admin = $1", user.Role)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get requests: ", err)
		return requests, err
	}
	for query.Next() {
		var request = new(models.Request)
		request.Document = new(models.Document)
		request.Initiator = new(models.User)
		request.Template = new(models.Template)
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
	requests := []*models.Request{}
	query, err := db.Query("SELECT * FROM requests WHERE initiator = $1", user.Uuid)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get requests")
		return requests, err
	}
	for query.Next() {
		var request = new(models.Request)
		request.Document = new(models.Document)
		request.Initiator = new(models.User)
		request.Template = new(models.Template)
		err = query.Scan(&request.Uuid, &request.Created, &request.Status, &request.Document.Uuid, &request.Initiator.Uuid, &request.Template.Uuid)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return nil, err
		}
		requests = append(requests, request)
	}
	return requests, err
}

func AddRequest(request models.Request) error {
	fmt.Print(request.Status)
	fmt.Print(request.Initiator.Uuid)
	fmt.Print(request.Template.Uuid)
	query, err := db.Query("INSERT INTO requests(status, initiator, template)  VALUES($1, $2, $3)", request.Status, request.Initiator.Uuid, request.Template.Uuid)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Cannot perform insert operation with template: ", err)
	}
	return err
}
