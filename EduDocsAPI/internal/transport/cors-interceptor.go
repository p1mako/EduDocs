package transport

import (
	"EduDocsAPI/internal/logger"
	"net/http"
	"slices"
)

var allowedOrigins = []string{"http://localhost:4200", "https://localhost:4200"}

func isAllowedOrigin(origin string) bool {
	return slices.Contains(allowedOrigins, origin)
}

func CorsInterceptor(next http.HandlerFunc) http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		logger.InfoLog.Printf("Got %s request from %s", request.Method, request.Header.Get("Origin"))
		if isAllowedOrigin(request.Header.Get("Origin")) {
			logger.InfoLog.Print("Setting headers for allowed referrer")
			writer.Header().Set("Access-Control-Allow-Origin", request.Header.Get("Origin"))
			writer.Header().Set("Access-Control-Allow-Headers", "*")
			writer.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE")
			if request.Method == http.MethodOptions {
				writer.WriteHeader(http.StatusAccepted)
				return
			}
		}
		next.ServeHTTP(writer, request)
	}
}
