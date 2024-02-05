package models

import (
	"EduDocsAPI/internal/models/enums"
	"time"
)

type Student struct {
	User           `json:"user"`
	EntryDate      time.Time           `json:"entryDate"`
	Group          int                 `json:"group"`
	Status         enums.StudentStatus `json:"status"`
	UniqueNumber   int                 `json:"uniqueNumber"`
	Specialization string              `json:"specialization"`
}
