package main

import (
	"EduDocsAPI/internal/app"
	"fmt"
	"golang.org/x/crypto/bcrypt"
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
	var password, _ = bcrypt.GenerateFromPassword([]byte("9iuo8nvw54n98e5"), -1)
	fmt.Print(string(password), "\n")
	initSwagger()
	app.Run()
}
