package transport

import (
	"EduDocsAPI/internal/logger"
	"encoding/json"
	"net/http"
)

func HandleLogin(rw http.ResponseWriter, request *http.Request) {
	user := authenticate(rw, request)
	userJson, err := json.Marshal(user)
	if err != nil {
		rw.WriteHeader(http.StatusInternalServerError)
		logger.ErrorLog.Print("Could not encode the answer for client: ", err)
		err := logger.LogResponseWriteError(rw.Write([]byte(err.Error())))
		if err != nil {
			rw.WriteHeader(http.StatusInternalServerError)
			return
		}
		return
	}
	err = logger.LogResponseWriteError(rw.Write(userJson))
	if err != nil {
		rw.WriteHeader(http.StatusInternalServerError)
		return
	}
}
