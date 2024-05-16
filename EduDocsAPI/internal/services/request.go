package services

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"errors"

	"github.com/google/uuid"
)

func GetAllRequests(username string) ([]*models.Request, error) {
	user, err := GetUserByLogin(username)
	if err != nil {
		logger.ErrorLog.Print("Cannot extract user for requests by login from db")

	}
	if user == nil {
		logger.ErrorLog.Print("Cannot get user for login: " + username)
	}
	student, professor, admin, err := GetExtendedUser(user)
	if err != nil {
		logger.ErrorLog.Print("Cannot extract corresponding role for user from db")
		return nil, err
	}
	var requests []*models.Request
	if admin != nil {
		requests, err = database.GetAllAvailableRequestsForAdmin(admin)
		if err != nil {
			logger.ErrorLog.Print("Cannot extract requests for admin in db")
			return nil, err
		}
	}
	if professor != nil {
		requests, err = database.GetAllAvailableRequestsForUser(professor.User)
		if err != nil {
			logger.ErrorLog.Print("Cannot extract requests for professor in db")
			return nil, err
		}
	}
	if student != nil {
		requests, err = database.GetAllAvailableRequestsForUser(student.User)
		if err != nil {
			logger.ErrorLog.Print("Cannot extract requests for student in db")
			return nil, err
		}
	}
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

func AddRequest(request models.Request) (error, []*models.Request) {
	if request.Initiator == nil || request.Template == nil {
		logger.ErrorLog.Print("Empty or unacceptable input for addition of request")
		return errors.New("empty or unacceptable input"), nil
	}
	err := database.AddRequest(request)
	if err != nil {
		logger.ErrorLog.Print("Could not add request to database")
		return err, nil
	}
	requests, err := GetAllRequests(request.Initiator.Login)
	if err != nil {
		return err, nil
	}
	return err, requests
}

func UpdateRequest(request models.Request) (error, []*models.Request) {
	if request.Initiator == nil || request.Template == nil || request.Uuid == uuid.Nil {
		logger.ErrorLog.Print("Empty or unacceptable input for update of the request")
		return errors.New("empty or unacceptable input"), nil
	}
	err := database.UpdateRequest(request)
	if err != nil {
		logger.ErrorLog.Print("Could not update request in database")
		return err, nil
	}
	requests, err := GetAllRequests(request.Initiator.Login)
	if err != nil {
		return err, nil
	}
	return err, requests
}
