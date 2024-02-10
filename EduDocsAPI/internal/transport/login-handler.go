package transport

import (
	"EduDocsAPI/internal/logger"
	"encoding/json"
	"net/http"
)

func HandleLogin(rw http.ResponseWriter, request *http.Request) {
	rw.Header().Set("Access-Control-Allow-Origin", "http://localhost:4200")

	rw.Header().Set("Access-Control-Allow-Headers", "*")
	rw.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE")
	if request.Method == http.MethodOptions {
		rw.WriteHeader(http.StatusAccepted)
		return
	}

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
