package database

import (
	"EduDocsAPI/internal/logger"
	"database/sql"
	"encoding/json"
	"fmt"
	_ "github.com/lib/pq"
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

const pathToConfig = "./internal/config/db-conf.json"

func init() {
	logger.InfoLog.Print("Loading database config from file...")
	connectionInfo, err := getConfig()
	if err != nil {
		logger.ErrorLog.Print("Cannot read db configuration because of:\n", err)
		return
	}
	connStr := parseToConStr(connectionInfo)
	logger.InfoLog.Print("Opening connection with database...")
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
	logger.InfoLog.Print("Closing database instance")
	err := db.Close()
	return err
}
