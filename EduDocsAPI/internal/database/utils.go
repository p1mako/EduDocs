package database

import (
	"EduDocsAPI/internal/logger"
	"database/sql"
)

func closeQuery(query *sql.Rows) {
	err := query.Close()
	if err != nil {
		logger.ErrorLog.Print("Cannot close connection: ", err)
	}
}
