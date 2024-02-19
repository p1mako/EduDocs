package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetTemplateById(id uuid.UUID) (*models.Template, error) {
	var template *models.Template
	query, err := db.Query("SELECT * FROM templates WHERE id = $1", id)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Error while extracting template: ", err)
		return nil, err
	}
	if !query.Next() {
		return nil, nil
	}
	err = query.Scan(&template.Uuid, &template.RouteToDocument, &template.Name, &template.ResponsibleAdmin)
	if err != nil {
		logger.ErrorLog.Print("Could not read input from query: ", err)
		return nil, err
	}
	return template, nil
}

func GetAllTemplates() ([]*models.Template, error) {
	templates := []*models.Template{}
	query, err := db.Query("SELECT * FROM templates")
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get templates")
		return nil, err
	}
	if !query.Next() {
		return templates, err
	}
	for query.Next() {
		var template *models.Template
		err = query.Scan(&template.Uuid, &template.RouteToDocument, &template.Name, &template.ResponsibleAdmin)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return nil, err
		}
		templates = append(templates, template)
	}
	return templates, err
}

func AddTemplate(template models.Template) error {
	query, err := db.Query("INSERT INTO templates(route_to_document, name, responsible_admin)  VALUES($1, $2, $3)", template.RouteToDocument, template.Name, template.ResponsibleAdmin)
	defer closeQuery(query)
	if err != nil {
		logger.ErrorLog.Print("Cannot perform insert operation with template: ", err)
	}
	return err
}
