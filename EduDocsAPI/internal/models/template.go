package models

type Template struct {
	Entity          `json:"entity"`
	Name            string `json:"name"`
	RouteToDocument string `json:"routeToDocument"`
}
