package transport

import (
	"EduDocsAPI/internal/models"
	"encoding/json"
	"net/http"
)

func handlePost(rw http.ResponseWriter, request *http.Request) {
	var user models.User
	var err = json.NewDecoder(request.Body).Decode(&user)
	if err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		_, _ = rw.Write([]byte("Wrong data provided"))
		return
	}
	if user.Password == "" || user.Login == "" {
		rw.WriteHeader(http.StatusBadRequest)
		_, _ = rw.Write([]byte("No login or password"))
		return
	}

}

func HandleLogin(rw http.ResponseWriter, request *http.Request) {
	//subtle.ConstantTimeCompare()
	login, password, ok := request.BasicAuth()
	if !ok {
		rw.WriteHeader(http.StatusUnauthorized)
	}

}
