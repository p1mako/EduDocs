package database

import (
	"EduDocsAPI/internal/logger"
	"database/sql"
	"encoding/json"
	"fmt"
	"os"
)

type dbInfo struct {
	Host     string `json:"host"`
	Port     int    `json:"port"`
	Dbname   string `json:"dbname"`
	User     string `json:"user"`
	Password string `json:"password"`
}

var db *sql.DB = nil

const pathToConfig = "../config/db-conf.json"

func init() {
	connectionInfo, err := getConfig()
	if err != nil {
		logger.ErrorLog.Print("Cannot read db configuration because of:\n", err)
		return
	}
	connStr := parseToConStr(connectionInfo)
	db, err = sql.Open("postgres", connStr)
	if err != nil {
		logger.ErrorLog.Print("Cannot connect to database with credentials: \n", *connectionInfo)
		logger.InfoLog.Print(err)
		return
	}
}

func getConfig() (*dbInfo, error) {
	var connectionInfo dbInfo
	confFile, err := os.Open(pathToConfig)
	if err != nil {
		return nil, err
	}
	err = json.NewDecoder(confFile).Decode(&connectionInfo)
	return &connectionInfo, err
}

func parseToConStr(connectionInfo *dbInfo) string {
	return fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable", connectionInfo.Host, connectionInfo.Port, connectionInfo.User, connectionInfo.Password, connectionInfo.Dbname)

}

func Close() error {
	err := db.Close()
	return err
}
