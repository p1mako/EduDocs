package models

import (
	"EduDocsAPI/internal/models/enums"
	"time"
)

type Request struct {
	Entity
	Status    enums.RequestStatus `json:"status"`
	Created   time.Time           `json:"created"`
	Document  *Document           `json:"document"`
	Template  *Template           `json:"template"`
	Initiator *User               `json:"initiator"`
}
