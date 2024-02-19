package models

type Professor struct {
	User
	Degree string `json:"degree"`
}
