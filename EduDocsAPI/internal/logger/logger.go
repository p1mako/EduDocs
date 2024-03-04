package logger

import (
	"log"
	"os"
)

var ErrorLog = log.New(os.Stderr, "ERROR\t", log.Ldate|log.Ltime|log.Lshortfile)
var InfoLog = log.New(os.Stdout, "INFO\t", log.Ldate|log.Ltime)

func LogResponseWriteError(_ int, err error) error {
	if err != nil {
		ErrorLog.Print("Could not write response to client: ", err)
	}
	return err
}
