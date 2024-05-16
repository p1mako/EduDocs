package transport

import (
	"EduDocsAPI/internal/logger"
	"net/http"
	"slices"
	"strings"
)

var allowedOrigins = []string{"http://localhost:4200", "https://localhost:4200"}
var allowedHeaders = []string{"Accept", "Accept-Encoding", "Accept-Language", "Authorization", "Cache-Control", "Connection", "Content-Type", "Host", "Origin", "Pragma", "Referer", "Sec-Fetch-Dest", "Sec-Fetch-Mode", "Sec-Fetch-Site", "User-Agent"}

func isAllowedOrigin(origin string) bool {
	return slices.Contains(allowedOrigins, origin)
}

func CorsInterceptor(next http.HandlerFunc) http.HandlerFunc {
	return func(writer http.ResponseWriter, request *http.Request) {
		logger.InfoLog.Printf("Got %s request from %s to the %s", request.Method, request.Header.Get("Origin"), request.RequestURI)
		if isAllowedOrigin(request.Header.Get("Origin")) {
			logger.InfoLog.Print("Setting headers for allowed referrer")
			writer.Header().Set("Access-Control-Allow-Origin", request.Header.Get("Origin"))
			writer.Header().Set("Access-Control-Allow-Headers", strings.Join(allowedHeaders, ", "))
			writer.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD, PUT, POST, DELETE")
			if request.Method == http.MethodOptions {
				writer.WriteHeader(http.StatusAccepted)
				return
			}
		}
		next.ServeHTTP(writer, request)
	}
}
