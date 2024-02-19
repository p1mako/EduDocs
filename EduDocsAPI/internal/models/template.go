package models

import "EduDocsAPI/internal/models/enums"

type Template struct {
	Entity           `json:"entity"`
	Name             string                   `json:"name"`
	RouteToDocument  string                   `json:"routeToDocument"`
	ResponsibleAdmin enums.AdministrationRole `json:"responsibleAdmin"`
}
