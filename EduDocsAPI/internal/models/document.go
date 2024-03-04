package models

import "time"

type Document struct {
	Entity
	Template     *Template `json:"template"`
	Created      time.Time `json:"created"`
	ValidThrough time.Time `json:"validThrough"`
	Author       *Admin    `json:"author"`
	Initiator    *User     `json:"initiator"`
}
