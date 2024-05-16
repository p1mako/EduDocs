package models

type User struct {
	Entity
	Login    string `json:"login"`
	Password string `json:"password"`
	Name     string `json:"name"`
	Surname  string `json:"surname"`
	LastName string `json:"lastName"`
}
