package app

import (
	"EduDocsAPI/internal/database"
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/transport"
	"fmt"
	"log"
	"net/http"
)

func Run() {
	defer afterServerEnded()
	setHandlers()

	servChan := make(chan error)
	userExit := make(chan struct{})
	go startServer(servChan)
	go waitUserInput(userExit)
	select {
	case servErr := <-servChan:
		logger.InfoLog.Print("Server ended execution")
		log.Fatal(servErr)
	case <-userExit:
		logger.InfoLog.Print("Exiting from user input")
		return
	}
}

func waitUserInput(c chan struct{}) {
	for {
		var cmd string
		_, _ = fmt.Scan(&cmd)
		if cmd == "exit" {
			close(c)
			return
		}
	}
}

func startServer(c chan error) {
	logger.InfoLog.Print("Started listening on :8080")
	err := http.ListenAndServe("localhost:8080", nil)
	c <- err
	close(c)
}

func setHandlers() {
	http.HandleFunc("/login", transport.CorsInterceptor(transport.HandleLogin))
	http.HandleFunc("/templates", transport.CorsInterceptor(transport.BasicAuth(transport.HandleTemplates)))
}

func afterServerEnded() {
	err := database.Close()
	if err != nil {
		logger.ErrorLog.Print("Could not close database connection")
		logger.InfoLog.Print(err)
	}
}
