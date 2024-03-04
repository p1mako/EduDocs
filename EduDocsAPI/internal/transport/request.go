package transport

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"EduDocsAPI/internal/services"
	"encoding/json"
	"net/http"
)

func handleAddRequest(w http.ResponseWriter, r *http.Request) {
	var request models.Request
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	err, requests := services.AddRequest(request)
	if err != nil {
		handleInternalError(err, w)
		return
	}
	w.WriteHeader(http.StatusOK)
	requestsJson, err := json.Marshal(requests)
	if err != nil {
		handleInternalError(err, w)
		return
	}
	_ = logger.LogResponseWriteError(w.Write(requestsJson))
}

func HandleAddRequest(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodPost:
		handleAddRequest(w, r)
	}
}
