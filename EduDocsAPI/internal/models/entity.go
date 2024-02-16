package models

import (
	"github.com/google/uuid"
)

type Entity struct {
	Uuid uuid.UUID `json:"uuid"`
}
