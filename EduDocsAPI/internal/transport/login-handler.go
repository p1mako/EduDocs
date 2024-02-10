package transport

import (
	"EduDocsAPI/internal/logger"
	"encoding/json"
	"net/http"
)

func HandleLogin(rw http.ResponseWriter, request *http.Request) {
	if request.Method != http.MethodGet {
		rw.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	user := authenticate(rw, request)
	login, _, _ := request.BasicAuth()
	userJson, err := json.Marshal(user)
	if err != nil {
		rw.WriteHeader(http.StatusInternalServerError)
		logger.ErrorLog.Print("Could not encode the answer for client: ", err)
		err := logger.LogResponseWriteError(rw.Write([]byte(err.Error())))
		if err != nil {
			rw.WriteHeader(http.StatusInternalServerError)
			return
		}
		logger.InfoLog.Print("Could not log in ", login)
		return
	}
	err = logger.LogResponseWriteError(rw.Write(userJson))
	if err != nil {
		rw.WriteHeader(http.StatusInternalServerError)
		logger.InfoLog.Print("Could not log in ", login)
		return
	}
	logger.InfoLog.Print("Logged in ", login)
}
