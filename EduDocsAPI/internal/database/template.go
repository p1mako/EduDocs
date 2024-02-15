package database

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"github.com/google/uuid"
)

func GetTemplateById(id uuid.UUID) (models.Template, error) {
	var template models.Template
	query, err := db.Query("SELECT * FROM templates WHERE id = $1", id)
	if err != nil {
		logger.ErrorLog.Print("Error while extracting template: ", err)
		return models.Template{}, err
	}
	if !query.Next() {
		logger.InfoLog.Printf("No data was extracted for %s", id)
	}
	err = query.Scan(&template.Uuid, &template.RouteToDocument, &template.Name, &template.ResponsibleAdmin)
	if err != nil {
		logger.ErrorLog.Print("Could not read input from query: ", err)
		return models.Template{}, err
	}
	return template, nil
}

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
		err = query.Scan(&template.Uuid, &template.RouteToDocument, &template.Name, &template.ResponsibleAdmin)
		if err != nil {
			logger.ErrorLog.Print("Model did not match the one in database")
			return templates, err
		}
		templates = append(templates, template)
	}
	return templates, err
}

func AddTemplate(template models.Template) error {
	_, err := db.Query("INSERT INTO templates(route_to_document, name, responsible_admin)  VALUES($1, $2, $3)", template.RouteToDocument, template.Name, template.ResponsibleAdmin)
	if err != nil {
		logger.ErrorLog.Print("Cannot add new template: ", err)
	}
	return err
}
