package models

type Specialization struct {
	Entity         `json:"entity"`
	Name           string `json:"name"`
	RegisterNumber int    `json:"registerNumber"`
}
