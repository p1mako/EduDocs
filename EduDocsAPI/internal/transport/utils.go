package transport

import (
	"EduDocsAPI/internal/logger"
	"net/http"
)

func handleInternalError(err error, w http.ResponseWriter) {
	w.WriteHeader(http.StatusInternalServerError)
	_ = logger.LogResponseWriteError(w.Write([]byte(err.Error())))
	return
}
