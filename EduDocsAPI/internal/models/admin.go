package models

import (
	"EduDocsAPI/internal/models/enums"
	"time"
)

type Admin struct {
	User
	Role               enums.AdministrationRole `json:"role"`
	From               time.Time                `json:"from"`
	Until              time.Time                `json:"until"`
	AvailableTemplates Template                 `json:"availableTemplates"`
}
