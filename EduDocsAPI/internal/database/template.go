package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
)

func GetAllTemplates() ([]models.Template, error) {
	var templates []models.Template
	query, err := db.Query("SELECT * FROM templates")
	if err != nil {
		logger.ErrorLog.Print("Could not execute query to get templates")
		return templates, err
	}
	if !query.Next() {
		logger.ErrorLog.Print("No data was extracted from query")
		return templates, err
	}
	for query.Next() {
		var template models.Template
		err = query.Scan(template.Uuid, template.RouteToDocument, template.Name)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return templates, err
		}
		templates = append(templates, template)
	}
	return templates, err
}

func AddTemplate(template models.Template) error {
	_, err := db.Query("INSERT INTO templates(route_to_document, name)  VALUES($1, $2)", template.RouteToDocument, template.Name)
	if err != nil {
		logger.ErrorLog.Print("Cannot add new template: ", err)
	}
	return err
}
