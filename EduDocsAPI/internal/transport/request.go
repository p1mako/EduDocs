package transport

import (
	"EduDocsAPI/internal/logger"
	"net/http"
)

func handleGetAllRequests(w http.ResponseWriter, r *http.Request) {
	user, _, ok := r.BasicAuth()
	if !ok {
		w.WriteHeader(http.StatusUnauthorized)
		logger.ErrorLog.Printf("User %s tried to access requests list without authorization. Seems to be a problem with auth module", user)
		return
	}
}

func HandleRequests(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodGet:
		handleGetAllRequests(w, r)
	default:
		w.WriteHeader(http.StatusMethodNotAllowed)
	}
}
