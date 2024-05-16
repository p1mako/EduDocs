package transport

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/services"
	"encoding/json"
	"net/http"
)

func HandleLogin(rw http.ResponseWriter, request *http.Request) {
	if request.Method != http.MethodGet {
		rw.WriteHeader(http.StatusMethodNotAllowed)
		return
	}
	user := authenticate(rw, request)
	student, professor, admin, err := services.GetExtendedUser(user)
	login, _, _ := request.BasicAuth()
	if user == nil {
		logger.InfoLog.Printf("Unauthenticated user with login %s", login)
		return
	}
	var userJson []byte
	if student != nil {
		userJson, err = json.Marshal(student)
	} else if admin != nil {
		userJson, err = json.Marshal(admin)
	} else {
		userJson, err = json.Marshal(professor)
	}
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
