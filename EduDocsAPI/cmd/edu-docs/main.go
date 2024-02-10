package main

import (
	"EduDocsAPI/internal/app"
	"net/http"
)

var swaggerHome = "/home/skalem/IdeaProjects/EduDocs/EduDocsAPI/web/dist"
var openapiHome = "/home/skalem/IdeaProjects/EduDocs/EduDocsAPI/api/"
var swaggerAddressPrefix = "/swaggerui/"
var openapiAddressPrefix = "/openapi/"

func initSwagger() {
	fs := http.FileServer(http.Dir(swaggerHome))
	swaggerFS := http.FileServer(http.Dir(openapiHome))
	http.Handle(swaggerAddressPrefix, http.StripPrefix(swaggerAddressPrefix, fs))
	http.Handle(openapiAddressPrefix, http.StripPrefix(openapiAddressPrefix, swaggerFS))
}

func main() {
	initSwagger()
	app.Run()
}
