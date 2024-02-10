package app

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/transport"
	"log"
	"net/http"
)

func Run() {
	defer func() {
		err := database.Close()
		if err != nil {
			logger.ErrorLog.Print("Could not close database connection")
			logger.InfoLog.Print(err)
		}
	}()

	http.HandleFunc("/login", transport.CorsInterceptor(transport.HandleLogin))
	logger.InfoLog.Print("Starting server...")
	log.Fatal(http.ListenAndServe("localhost:8080", nil))
}
