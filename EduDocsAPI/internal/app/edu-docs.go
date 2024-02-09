package app

import (
	"EduDocsAPI/internal/transport"
	"log"
	"net/http"
)

func Run() {
	http.HandleFunc("POST /login", transport.HandleLogin)
	http.HandleFunc("GET /logout", transport)
	log.Default().Print("Starting server...")
	log.Fatal(http.ListenAndServe("localhost:8080", nil))
}
