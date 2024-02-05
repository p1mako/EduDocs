package app

import (
	"log"
	"net/http"
)

func Run() {
	log.Default().Print("Starting server...")
	log.Fatal(http.ListenAndServe("localhost:8080", nil))
}
