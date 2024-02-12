package transport

import (
	"EduDocsAPI/internal/logger"
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

func HandleTemplates(rw http.ResponseWriter, request *http.Request) {
	switch request.Method {
	case http.MethodGet:
		getTemplates(rw, request)
	default:
		rw.WriteHeader(http.StatusMethodNotAllowed)
	}
}
