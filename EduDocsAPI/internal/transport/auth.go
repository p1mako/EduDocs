package transport

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"EduDocsAPI/internal/services"
	"golang.org/x/crypto/bcrypt"
	"net/http"
)

func BasicAuth(next http.HandlerFunc) http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		username, _, _ := request.BasicAuth()
		user := authenticate(writer, request)
		if user != nil {
			logger.InfoLog.Print("Authenticated user with login ", username)
			next.ServeHTTP(writer, request)
		} else {
			writer.Header().Set("WWW-Authenticate", `Basic realm="restricted", charset="UTF-8"`)
			http.Error(writer, "Unauthorized", http.StatusUnauthorized)
			logger.InfoLog.Printf("Unauthenticated user with login %s", username)
		}
	}
}

func authenticate(writer http.ResponseWriter, request *http.Request) *models.User {
	login, password, ok := request.BasicAuth()
	if !ok {
		writer.WriteHeader(http.StatusUnauthorized)
		return nil
	}
	user, err := services.GetUserByLogin(login)
	if err != nil {
		logger.ErrorLog.Print(err)
		writer.WriteHeader(http.StatusInternalServerError)
		_, err = writer.Write([]byte(err.Error()))
		if err != nil {
			logger.ErrorLog.Print("Cannot respond to client: ", err)
		}
		return nil
	}
	err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(password))
	if err != nil {
		writer.WriteHeader(http.StatusUnauthorized)
		_, err = writer.Write([]byte(err.Error()))
		if err != nil {
			logger.ErrorLog.Print("Cannot respond to client: ", err)
			writer.WriteHeader(http.StatusInternalServerError)
		}
		return nil
	}
	return &user
}
