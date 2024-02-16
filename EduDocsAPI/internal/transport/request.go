package transport

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/services"
	"encoding/json"
	"fmt"
	"net/http"
)

func handleGetAllRequests(w http.ResponseWriter, r *http.Request) {
	user, _, ok := r.BasicAuth()
	if !ok {
		w.WriteHeader(http.StatusUnauthorized)
		logger.ErrorLog.Printf("User %s tried to access requests list without authorization. Seems to be a problem with auth module", user)
		return
	}
	requests, err := services.GetAllRequests(user)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		_ = logger.LogResponseWriteError(w.Write([]byte(err.Error())))
		return
	}

	jsonRequests, err := json.Marshal(requests)
	fmt.Print(string(jsonRequests))
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
	_ = logger.LogResponseWriteError(w.Write(jsonRequests))
}

func HandleRequests(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		handleGetAllRequests(w, r)
	default:
		w.WriteHeader(http.StatusMethodNotAllowed)
	}
}
