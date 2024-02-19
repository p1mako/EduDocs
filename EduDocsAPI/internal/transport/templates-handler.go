package transport

import (
	"EduDocsAPI/internal/logger"
	"EduDocsAPI/internal/models"
	"EduDocsAPI/internal/services"
	"encoding/json"
	"net/http"
)

func getTemplates(rw http.ResponseWriter, request *http.Request) {
	templates, err := services.GetAllTemplates()
	if err != nil {
		handleInternalError(err, rw)
		return
	}
	templatesJson, err := json.Marshal(templates)
	if err != nil {
		handleInternalError(err, rw)
		return
	}
	_ = logger.LogResponseWriteError(rw.Write(templatesJson))
}

func addTemplate(w http.ResponseWriter, r http.Request) {
	var template models.Template
	err := json.NewDecoder(r.Body).Decode(&template)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}
	err = services.AddTemplate(template)
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
	}
	w.WriteHeader(http.StatusOK)
}

func HandleGetTemplates(rw http.ResponseWriter, request *http.Request) {
	switch request.Method {
	case http.MethodGet:
		getTemplates(rw, request)
	default:
		rw.WriteHeader(http.StatusMethodNotAllowed)
	}

}

func HandleAddTemplates(w http.ResponseWriter, r http.Request) {
	switch r.Method {
	case http.MethodPost:
		addTemplate(w, r)
	default:
		w.WriteHeader(http.StatusMethodNotAllowed)
	}
}
