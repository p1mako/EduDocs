package models

import (
	"EduDocsAPI/internal/models/utils"
	"time"
)

type Document struct {
	Entity
	Template     *Template  `json:"template"`
	Created      time.Time  `json:"created"`
	ValidThrough utils.Date `json:"validThrough"`
	Author       *Admin     `json:"author"`
	Initiator    *User      `json:"initiator"`
}
